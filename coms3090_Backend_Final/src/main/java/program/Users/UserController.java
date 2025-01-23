package program.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import program.Posts.Service.*;
import program.Posts.Post;
import program.Posts.DTOs.PostDTOs.PostDTO;
import program.Posts.Repositories.PostRepository;
import program.Users.Service.UserService;
import program.Users.dto.UserChangeRequest;
import program.Users.dto.UserDTO;
import program.chatRooms.chatRoomControl.ChatRoom;
import program.Posts.Post;
import program.Posts.Service.PostService;


import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PostRepository postRepository;

    @GetMapping()
    ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> found = userRepository.findAll();
        List<UserDTO> users = new ArrayList<>();
        for (User u : found) {
            users.add(UserDTO.turnToDTO(u));
        }
        return new ResponseEntity(users, HttpStatus.OK);
    }

    @GetMapping(path = "/{userId}/posts")
    ResponseEntity<List<PostDTO>> getPostsFromUser(@PathVariable Integer userId){
        try{
            List<PostDTO> posts = userService.getAllPostsFromUser(userId);
            return ResponseEntity.status(HttpStatus.OK).body(posts);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    @PutMapping()
    ResponseEntity<String> changeUserRequest(@RequestBody UserChangeRequest userChangeRequest){
        try{
            userService.userChangeRequest(userChangeRequest);
            return ResponseEntity.status(HttpStatus.OK).body("Changes successfully");
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(path = "/{id}")
    ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
        Optional<User> found = userRepository.findByUserId(id);
        if (!found.isEmpty()) return new ResponseEntity(UserDTO.turnToDTO(found.get()), HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    ResponseEntity createNewUser(@RequestBody User user) {
        if (user == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    ResponseEntity deleteUser(@PathVariable int id) {
        userRepository.deleteByUserId(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/rooms/{userId}")
    public ResponseEntity<List<Integer>> getRoomsByUserId(@PathVariable Integer userId) {
        // Get user from path variable and make sure it exists
        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        // Create an accumulator for room ids
        List<Integer> roomIds = new ArrayList<>();
        // Add the room ids for all the rooms the user is in
        for(ChatRoom c : user.get().getChatRooms()) {
            roomIds.add(c.getRoomId());
        }

        return new ResponseEntity<>(roomIds, HttpStatus.OK);
    }

    @GetMapping(path="/followers/{userId}")
    public ResponseEntity<List<UserDTO>> getFollowers(@PathVariable Integer userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        List<UserDTO> users = new ArrayList<>();
        for (User u : user.get().getFollowers()) {
            users.add(UserDTO.turnToDTO(u));
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path="/following/{userId}")
    public ResponseEntity<List<UserDTO>> getFollowing(@PathVariable Integer userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        List<UserDTO> users = new ArrayList<>();
        for (User u : user.get().getFollowing()) {
            users.add(UserDTO.turnToDTO(u));
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping(path = "/{followerId}/follow/{followingId}")
    public ResponseEntity followUser(@PathVariable Integer followerId, @PathVariable Integer followingId) {
        Optional<User> follower = userRepository.findByUserId(followerId);
        Optional<User> following = userRepository.findByUserId(followingId);
        if (follower.isEmpty() || following.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        follower.get().addFollower(following.get());
        following.get().follow(follower.get());
        userRepository.save(follower.get());
        userRepository.save(following.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{followerId}/follow/{followingId}")
    public ResponseEntity unFollowUser(@PathVariable Integer followerId, @PathVariable Integer followingId) {
        Optional<User> follower = userRepository.findByUserId(followerId);
        Optional<User> following = userRepository.findByUserId(followingId);
        if (follower.isEmpty() || following.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        follower.get().removeFollower(following.get());
        following.get().unfollow(follower.get());
        userRepository.save(follower.get());
        userRepository.save(following.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping(path = "/following/{userId}/posts")
    public ResponseEntity<List<PostDTO>> getPostsFromUsersFollowing(@PathVariable Integer userId) {
        // Get user from path variable and make sure it exists
        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        List<PostDTO> posts = new ArrayList<>();
        for (User u : user.get().getFollowing()) {
            for (Post p : u.getPosts()) {
                if (p.getGroup().isEmpty()) {
                    posts.add(PostService.convertPostToPostDTO(p));
                }
            }
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping(path = "/liked/{userId}/posts")
    public ResponseEntity<List<PostDTO>> getLikedPostByUser(@PathVariable Integer userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        List<PostDTO> posts = new ArrayList<>();
        for (Post p : user.get().getLikedPosts()) {
                if (p.getGroup().isEmpty()) {
                    posts.add(PostService.convertPostToPostDTO(p));
                }
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping(path = "/likedBy/{postId}")
    public ResponseEntity<List<UserDTO>> getUsersWhoLikedPost(@PathVariable Integer postId) {
        Optional<Post> post = postRepository.findByPostId(postId);
        if (post.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        List<UserDTO> users = new ArrayList<>();
        for (User u : post.get().getLikedBy()) {
            users.add(UserDTO.turnToDTO(u));
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

}
