package program.Users.Service;

import program.Users.User;
import program.Users.UserRepository;
import program.Users.dto.UserChangeRequest;
import program.Groups.Group;
import program.Posts.Post;
import program.Posts.DTOs.PostDTOs.PostDTO;
import program.Posts.Repositories.PostRepository;
import program.Posts.Service.PostService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostService postService;

    @Autowired
    public UserService(PostRepository postRepository, UserRepository userRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }
    public List<PostDTO> getAllPostsFromUser(Integer userId){
        User user = userRepository.findByUserId(userId).get();
        List<Post> posts = postRepository.findAllByUser(user);
        List<PostDTO> postDTOs = new ArrayList<>();
        posts.forEach((post) -> {
            postDTOs.add(postService.convertPostToPostDTO(post));
        });
        return postDTOs;

    }
    public void userChangeRequest(UserChangeRequest userChangeRequest){
        if(!userRepository.existsByUserId(userChangeRequest.getUserId())){
            throw new IllegalArgumentException("User does not exist");
        }
        User user = userRepository.findByUserId(userChangeRequest.getUserId()).get();
                //if user is changing the username and the username is already taken
        if(userChangeRequest.getUsername() != null && !userRepository.findByUsername(userChangeRequest.getUsername()).isEmpty()){
            throw new IllegalArgumentException("Username is already taken");
        }
        //if user is changing username
        if(userChangeRequest.getUsername() != null){
            user.setUsername(userChangeRequest.getUsername());
        }
        //if user is changing email and email is used
        if(userChangeRequest.getEmail() != null && !userRepository.findByEmail(userChangeRequest.getEmail()).isEmpty()){
            throw new IllegalArgumentException("Email is already used");
        }

        if(userChangeRequest.getEmail() != null){
            user.setEmail(userChangeRequest.getEmail());
        }

        if(userChangeRequest.getUniversity() != null){
            user.setUniversity(userChangeRequest.getUniversity());
        }

        if(userChangeRequest.getPassword() != null){
            user.setPassword(userChangeRequest.getPassword());
        }
        userRepository.save(user);
    }
}