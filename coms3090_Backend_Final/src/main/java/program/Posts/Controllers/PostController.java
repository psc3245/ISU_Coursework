package program.Posts.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import program.Groups.Group;
import program.Groups.GroupRepository;
import program.Groups.Service.GroupService;
import program.Groups.dto.AddPostRequest;
import program.Posts.Post;
import program.Posts.DTOs.CreationRequests.PostCreationRequest;
import program.Posts.DTOs.PostDTOs.PostDTO;
import program.Posts.DTOs.UpdateRequests.PostUpdateRequest;
import program.Posts.Repositories.PostRepository;
import program.Posts.Service.PostService;
import program.Users.User;
import program.Users.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupService groupService;

    //This endpoint is more so meant for the backend to use
    @GetMapping(path = "/api/posts")
    ResponseEntity<List<PostDTO>> getAllPosts() {
        List<Post> results = postRepository.findAll();
        List<PostDTO> postDTOs = new ArrayList<PostDTO>();
        for(Post p : results) {
            if (p != null) {
                PostDTO temp = postService.convertPostToPostDTO(p);
                postDTOs.add(temp);
            }
        }
        List<PostDTO> toReturn = PostService.sortPostDTOsByDate(postDTOs);
        return new ResponseEntity(toReturn, HttpStatus.OK);
    }

    @GetMapping(path = "/api/posts/sorted")
    ResponseEntity<List<PostDTO>> getAllPostsSortByLikes() {
        List<Post> results = postRepository.findAll();
        List<PostDTO> postDTOs = new ArrayList<PostDTO>();
        for(Post p : results) {
            if (p != null) {
                PostDTO temp = postService.convertPostToPostDTO(p);
                postDTOs.add(temp);
            }
        }
        List<PostDTO> toReturn = PostService.sortPostDTOByLikes(postDTOs);
        return new ResponseEntity(toReturn, HttpStatus.OK);
    }

    //The post by ID is meant to be used by the front end when opening a post to get more specific information about said post.
    @GetMapping(path = "/api/posts/{id}")
    ResponseEntity<PostDTO> getPostById(@PathVariable int id) {
        Optional<Post> found = postRepository.findByPostId(id);
        if (!found.isEmpty()) {
            PostDTO postdto = postService.convertPostToRelevantPostDTO(found.get());
            return new ResponseEntity(postdto, HttpStatus.OK);
        }
        // Empty optional, return null
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    //THis endpoint is how we create posts, a PostCreationRequest is simply an object which has the required information to create the specified type of post
    @PostMapping(path = "/api/posts")
    ResponseEntity createPost(@RequestBody @Valid PostCreationRequest postCreationRequest) {
        try{
            Post p = postService.createPost(postCreationRequest);
            return new ResponseEntity(HttpStatus.CREATED);
        }
        catch(IllegalArgumentException | IllegalAccessException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    //This endpoint is how we update posts, generally these requests are the exact same as creationRequests without the userId and other starting values that shouldn't be changed
    @PutMapping(path = "/api/posts/{id}")
    ResponseEntity updatePost(@PathVariable Integer id, @RequestBody PostUpdateRequest updateRequest) {
        try{
            PostDTO newPost = postService.updatePost(updateRequest, id);
            return new ResponseEntity(newPost, HttpStatus.OK);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    //Very simple delete endpoint
    @DeleteMapping(path = "/api/posts/{id}")
    ResponseEntity deletePost(@PathVariable Integer id) {
        postRepository.deleteByPostId(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "/api/posts/{userId}/like/{postId}")
    ResponseEntity likePost(@PathVariable Integer userId, @PathVariable Integer postId) {
        Optional<User> user = userRepository.findByUserId(userId);
        Optional<Post> post = postRepository.findByPostId(postId);
        if (user.isEmpty() || post.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        // Both user and post exist
        user.get().likePost(post.get());
        post.get().userLikePost(user.get());
        userRepository.save(user.get());
        postRepository.save(post.get());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(path = "/api/posts/{userId}/unlike/{postId}")
    ResponseEntity unlikePost(@PathVariable Integer userId, @PathVariable Integer postId) {
        Optional<User> user = userRepository.findByUserId(userId);
        Optional<Post> post = postRepository.findByPostId(postId);
        if (user.isEmpty() || post.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        // Both user and post exist
        user.get().unlikePost(post.get());
        post.get().userUnLikePost(user.get());
        userRepository.save(user.get());
        postRepository.save(post.get());
        return new ResponseEntity(HttpStatus.OK);
    }
}
