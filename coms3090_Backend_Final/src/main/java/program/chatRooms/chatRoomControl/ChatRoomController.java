package program.chatRooms.chatRoomControl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import program.Users.User;
import program.Users.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/rooms")
public class ChatRoomController {

    @Autowired
    UserRepository userRepository;
    ChatRoomRepository chatRoomRepository;

    public ChatRoomController(UserRepository userRepository,  ChatRoomRepository chatRoomRepository) {
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
    }


    @GetMapping(path = "/{roomId}")
    public ResponseEntity<List<Integer>> getUsersByRoomId(@PathVariable int roomId) {
        // Get the room from path variable and make sure it exists
        Optional<ChatRoom> chatRoom = chatRoomRepository.findRoomByRoomId(roomId);
        if (chatRoom.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        // Make an accumulator for user ids
        List<Integer> users = new ArrayList<>();

        // Add ids for each user
        for (User u : chatRoom.get().getUsersInRoom()) {
            users.add(u.getUserId());
        }

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Integer> createNewChatRoom() {
        // Make a new chat room
        ChatRoom c = new ChatRoom();
        // Save it in repository
        chatRoomRepository.save(c);
        // Return room id
        return new ResponseEntity<>(c.getRoomId(), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{roomId}/{userId}")
    public ResponseEntity addUserToRoom(@PathVariable Integer roomId, @PathVariable Integer userId) {
        // Get the user and room from path variables
        Optional<ChatRoom> c = chatRoomRepository.findRoomByRoomId(roomId);
        Optional<User> u = userRepository.findByUserId(userId);

        // Make sure both ids correspond to existing entities
        if (u.isEmpty() || c.isEmpty()) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        // Add the user to room
        c.get().addUserToRoom(u.get());

        chatRoomRepository.save(c.get());

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{roomId}")
    public ResponseEntity deleteChatRoom(@PathVariable Integer roomId) {
        // Get room from path variable
        Optional<ChatRoom> c = chatRoomRepository.findRoomByRoomId(roomId);

        // Make sure room exists
        if (c.isEmpty()) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        // Go through each user in the room and remove the room from their list of rooms
        for (User u : c.get().getUsersInRoom()) {
            List<ChatRoom> rooms = u.getChatRooms();
            rooms.remove(c);
            u.setChatRooms(rooms);
        }

        // Delete the chat room from the DB
        chatRoomRepository.delete(c.get());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/{roomId}/{userId}")
    public ResponseEntity removeUserFromRoom(@PathVariable Integer roomId, @PathVariable Integer userId) {
        // Get the room and user from path variables
        Optional<ChatRoom> c = chatRoomRepository.findRoomByRoomId(roomId);
        Optional<User> u = userRepository.findByUserId(userId);

        // Check to make sure both id's correspond to existing entities
        if (u.isEmpty() || c.isEmpty()) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        // Remove the room from the user's chat room list
        List<ChatRoom> rooms = u.get().getChatRooms();
        rooms.remove(c.get());
        u.get().setChatRooms(rooms);

        // Remove the user from the chat room's list
        List<User> users = c.get().getUsersInRoom();
        users.remove(u.get());
        c.get().setUsersInRoom(users);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
