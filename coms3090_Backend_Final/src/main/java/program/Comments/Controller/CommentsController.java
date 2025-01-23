package program.Comments.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import program.Comments.CommentService;
import program.Comments.DTOs.CommentCreationRequest;
import program.Comments.DTOs.CommentDTO;
import program.Comments.DTOs.CommentUpdateRequest;


@RestController
@RequestMapping("/api/comments")
public class CommentsController {
    private CommentService commentService;
    @Autowired
    public CommentsController(CommentService commentService){
        this.commentService = commentService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllComments(){
        try{
            return new ResponseEntity<List<CommentDTO>>(commentService.getAllComments(), HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getAllCommentsForAPost(@PathVariable Integer postId){
        try{
            return new ResponseEntity<List<CommentDTO>>(commentService.getAllCommentsForAPost(postId), HttpStatus.OK);
        }
        catch(IllegalArgumentException e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllCommentsForAUser(@PathVariable Integer userId){
        try{
            return new ResponseEntity<List<CommentDTO>>(commentService.getAllCommentsForAUser(userId), HttpStatus.OK);
        }
        catch(IllegalArgumentException e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{postId}/{userId}")
    public ResponseEntity<?> getAllCommentsForAUserOnAPost(@PathVariable Integer userId, @PathVariable Integer postId){
        try{
            return new ResponseEntity<List<CommentDTO>>(commentService.getAllCommentsForAUserOnAPost(userId,postId), HttpStatus.OK);
        }
        catch(IllegalArgumentException e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<?> createComment(@RequestBody CommentCreationRequest commentCreationRequest){
        try{
            return new ResponseEntity<CommentDTO>(commentService.createComment(commentCreationRequest), HttpStatus.OK);
        }
        catch(IllegalArgumentException e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateComment(@RequestBody CommentUpdateRequest commentUpdateRequest){
        try{
            return new ResponseEntity<CommentDTO>(commentService.updateComment(commentUpdateRequest), HttpStatus.OK);
        }
        catch(IllegalArgumentException e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer commentId){
        try{
            return new ResponseEntity<String>(commentService.deleteComment(commentId), HttpStatus.OK);
        }
        catch(IllegalArgumentException e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{commentId}/like/{userId}")
    public ResponseEntity<?> likeComment(@PathVariable Integer commentId, @PathVariable Integer userId) {
        try{
            commentService.likeComment(commentId, userId);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch(IllegalArgumentException e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{commentId}/unlike/{userId}")
    public ResponseEntity<?> unlikeComment(@PathVariable Integer commentId, @PathVariable Integer userId) {
        try{
            commentService.unlikeComment(commentId, userId);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch(IllegalArgumentException e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sorted")
    public ResponseEntity<?> getAllCommentsSortedByLikes(){
        try{
            List<CommentDTO> toRet = CommentService.sortCommentDTOByLikes(commentService.getAllComments());
            return new ResponseEntity<List<CommentDTO>>(toRet, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{postId}/sorted")
    public ResponseEntity<?> getAllCommentsForAPostSortedByLikes(@PathVariable Integer postId){
        try{
            List<CommentDTO> toRet = CommentService.sortCommentDTOByLikes(commentService.getAllCommentsForAPost(postId));
            return new ResponseEntity<List<CommentDTO>>(toRet, HttpStatus.OK);
        }
        catch(IllegalArgumentException e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}/sorted")
    public ResponseEntity<?> getAllCommentsForAUserSortedByLikes(@PathVariable Integer userId){
        try{
            List<CommentDTO> toRet = CommentService.sortCommentDTOByLikes(commentService.getAllCommentsForAUser(userId));
            return new ResponseEntity<List<CommentDTO>>(toRet, HttpStatus.OK);
        }
        catch(IllegalArgumentException e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{postId}/{userId}/sorted")
    public ResponseEntity<?> getAllCommentsForAUserOnAPostSortedByLikes(@PathVariable Integer userId, @PathVariable Integer postId){
        try{
            List<CommentDTO> toRet = CommentService.sortCommentDTOByLikes(commentService.getAllCommentsForAUserOnAPost(userId,postId));
            return new ResponseEntity<List<CommentDTO>>(toRet, HttpStatus.OK);
        }
        catch(IllegalArgumentException e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
