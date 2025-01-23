package program.Comments;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import program.Comments.DTOs.CommentCreationRequest;
import program.Comments.DTOs.CommentDTO;
import program.Comments.DTOs.CommentUpdateRequest;
import program.Posts.Post;
import program.Posts.Repositories.PostRepository;
import program.Users.User;
import program.Users.UserRepository;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository){
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }
    public List<CommentDTO> getAllComments(){
        return CommentDTO.CommentListToCommentDTOList(commentRepository.findAll());
    }
    public List<CommentDTO> getAllCommentsForAPost(Integer postId) throws IllegalArgumentException{
        Optional<Post> post = postRepository.findByPostId(postId);
        if(!post.isPresent()){
            throw new IllegalArgumentException("There is no post with that postId");
        }
        return CommentDTO.CommentListToCommentDTOList(commentRepository.findAllByPost(post.get()));
    }
    public List<CommentDTO> getAllCommentsForAUser(Integer userId) throws IllegalArgumentException{
        Optional<User> user = userRepository.findByUserId(userId);
        if(!user.isPresent()){
            throw new IllegalArgumentException("The User does not exist");
        }
        return CommentDTO.CommentListToCommentDTOList(commentRepository.findAllByCommenter(user.get()));
    }
    public List<CommentDTO> getAllCommentsForAUserOnAPost(Integer userId, Integer postId) throws IllegalArgumentException{
        var postO = postRepository.findByPostId(postId);
        if (!postO.isPresent()){
            throw new IllegalArgumentException("Post does not Exist");
        }
        List<Comment> comments = commentRepository.findAllByPost(postO.get());
        List<Comment> filtered = new ArrayList<>();
        for (Comment comment : comments) {
            if(comment.getCommenter().getUserId() == userId){
                filtered.add(comment);
            }
        }
        return CommentDTO.CommentListToCommentDTOList(filtered);
    }
    public CommentDTO createComment(CommentCreationRequest commentCreationRequest) throws IllegalArgumentException{
        Comment comment = new Comment();
        var userO = userRepository.findByUserId(commentCreationRequest.getUserId());
        if (!userO.isPresent()){
            throw new IllegalArgumentException("The user does not exist");
        }
        comment.setCommenter(userO.get());
        comment.setContent(commentCreationRequest.getContent());
        comment.setDate(LocalDateTime.now());
        var postO = postRepository.findByPostId(commentCreationRequest.getPostId());
        if (!postO.isPresent()){
            throw new IllegalArgumentException("The post does not exist");
        }
        comment.setPost(postO.get());
        commentRepository.save(comment);
        return CommentDTO.CommentToCommentDTO(comment);
    }
    public CommentDTO updateComment(CommentUpdateRequest commentUpdateRequest) throws IllegalArgumentException{
        var commentO = commentRepository.findById(commentUpdateRequest.getCommentId());
        if(!commentO.isPresent()){
            throw new IllegalArgumentException("Comment Does not exist");
        }
        Comment comment = commentO.get();
        if(commentUpdateRequest.getContent() != null){
            comment.setContent(commentUpdateRequest.getContent());
        }
        commentRepository.save(comment);
        return CommentDTO.CommentToCommentDTO(comment);
    }
    public String deleteComment(Integer commentId) throws IllegalArgumentException{
        var commentO = commentRepository.findById(commentId);
        if (!commentO.isPresent()){
            throw new IllegalArgumentException("The comment doesn't exist");
        }
        commentRepository.delete(commentO.get());
        return "Success";
    }

    public void likeComment(Integer commentId, Integer userId) throws IllegalArgumentException {
        var comment0 = commentRepository.findById(commentId);
        var user0 = userRepository.findByUserId(userId);

        if(comment0.isEmpty()) throw new IllegalArgumentException("The comment doesn't exist");
        if(user0.isEmpty()) throw new IllegalArgumentException("The user doesn't exist");

        Comment comment = comment0.get();
        User user = user0.get();

        Set<User> newLikedBy = comment.getLikedBy();
        newLikedBy.add(user);
        comment.setLikedBy(newLikedBy);

        commentRepository.save(comment);

        Set<Comment> newLiked = user.getLikedComments();
        newLiked.add(comment);
        user.setLikedComments((newLiked));

        userRepository.save(user);
    }

    public void unlikeComment(Integer commentId, Integer userId) throws IllegalArgumentException {
        var comment0 = commentRepository.findById(commentId);
        var user0 = userRepository.findByUserId(userId);

        if(comment0.isEmpty()) throw new IllegalArgumentException("The comment doesn't exist");
        if(user0.isEmpty()) throw new IllegalArgumentException("The user doesn't exist");

        Comment comment = comment0.get();
        User user = user0.get();

        Set<User> newLikedBy = comment.getLikedBy();
        newLikedBy.remove(user);
        comment.setLikedBy(newLikedBy);

        commentRepository.save(comment);

        Set<Comment> newLiked = user.getLikedComments();
        newLiked.remove(comment);
        user.setLikedComments((newLiked));

        userRepository.save(user);
    }

    public static List<CommentDTO> sortCommentDTOByLikes(List<CommentDTO> comments) {
        Collections.sort(comments, new CommentDTOLikeComparator());
        return comments;
    }
}
