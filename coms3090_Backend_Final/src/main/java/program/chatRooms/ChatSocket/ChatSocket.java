package program.chatRooms.ChatSocket;


import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import program.Users.User;
import program.Users.UserRepository;

@Controller
@ServerEndpoint(value = "/api/chat/{roomId}/{userId}")
public class ChatSocket {

    private static MessageRepository msgRepo;
    private static UserRepository userRepository;

    @Autowired
    public void setMessageRepository(MessageRepository repo, UserRepository userRepository) {
        msgRepo = repo;
        this.userRepository = userRepository;
    }

    private static Map<Integer, Map<Session, String>> roomSessionMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(ChatSocket.class);

    @OnOpen
    public void OnOpen(Session session, @PathParam("roomId") int roomId, @PathParam("userId") int userId) throws IOException {

        logger.info("Entered into Room " + roomId);

        //Check to see if user exists
        Optional<User> u = userRepository.findByUserId(userId);
        if (u.isEmpty()) {
            logger.info("User not found");
            // This may work ??
            session.close();
            return;
        }

        // Now we know user exists, get their username
        String username = u.get().getUsername();

        // store connecting user information
        usernameSessionMap.put(username, session);
        roomSessionMap.computeIfAbsent(roomId, k -> new Hashtable<>()).put(session, username);

        //Send chat history to the newly connected user
        sendMessageToParticularUser(username, getChatHistory());

        // broadcast that new user joined
        String message = "User " + username + " has Joined the Chat";
        broadcast(roomId, message);
    }


    @OnMessage
    public void onMessage(Session session, String message, @PathParam("roomId") int roomId) throws IOException {

        // Handle new messages
        logger.info("Entered into Message: Got Message:" + message);
        String username = getUsernameFromSession(session, roomId);

        // Broadcast
        broadcast(roomId, message);

        // Saving chat history to repository
        msgRepo.save(new Message(username, message));
    }


    @OnClose
    public void onClose(Session session, @PathParam("roomId") int roomId) throws IOException {
        if (!session.isOpen()) {
            System.out.println("test");
            return;
        }

        logger.info("Entered into Close");

        // remove the user connection information
        String username = getUsernameFromSession(session, roomId);
        roomSessionMap.get(roomId).remove(session);
        usernameSessionMap.remove(username);

        // broadcase that the user disconnected
        String message = username + " disconnected";
        broadcast(roomId, message);
    }


    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        logger.info("Entered into Error");
        throwable.printStackTrace();
    }

    private String getUsernameFromSession(Session session, int roomId) {
        // Get session map from hashtable with room id
        Map<Session, String> sessionMap = roomSessionMap.get(roomId);
        // Ensure both the session map exists and the given session exists in the map
        if (sessionMap == null || sessionMap.get(session) == null) return null;
        // Return the username
        return sessionMap.get(session);
    }

    private void sendMessageToParticularUser(String username, String message) {
        try {
            usernameSessionMap.get(username).getBasicRemote().sendText(message);
        }
        catch (IOException e) {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
    }

    private void broadcast(int roomId, String message) {
        Map<Session, String> sessionMap = roomSessionMap.get(roomId);
        sessionMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            }
            catch (IOException e) {
                logger.info("Exception: " + e.getMessage().toString());
                e.printStackTrace();
            }

        });
    }

    private String getChatHistory() {
        List<Message> messages = msgRepo.findAll();

        // convert the list to a string
        StringBuilder sb = new StringBuilder();
        if(messages != null && messages.size() != 0) {
            for (Message message : messages) {
                sb.append(message.getUsername() + ": " + message.getContent() + "\n");
            }
        }
        return sb.toString();
    }


}
