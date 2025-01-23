package program.Comments.DTOs;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import program.Comments.Comment;
import program.Posts.Post;
import program.Users.User;

public class CommentDTO {
    private Integer commentId;
    private String content; 
    private Integer userId;
    private String username;
    private Integer postId;
    private LocalDateTime date;
    private Set<Integer> likedByUserIds = new HashSet<>();
    public CommentDTO(Integer commentId, String content, Integer userId, String username, Integer postId,
            LocalDateTime date) {
        this.commentId = commentId;
        this.content = content;
        this.userId = userId;
        this.username = username;
        this.postId = postId;
        this.date = date;
    }
    public CommentDTO() {
    }
    public Integer getCommentId() {
        return commentId;
    }
    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Integer getPostId() {
        return postId;
    }
    public void setPostId(Integer postId) {
        this.postId = postId;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public Set<Integer> getLikedByUserIds() { return likedByUserIds; }
    public void setLikedByUserIds(Set<Integer> likedByUserIds) { this.likedByUserIds = likedByUserIds; }
    
    public static CommentDTO CommentToCommentDTO(Comment c){
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentId(c.getCommentId());
        commentDTO.setContent(c.getContent());
        commentDTO.setDate(c.getDate());
        commentDTO.setPostId(c.getPost().getPostId());
        commentDTO.setUserId(c.getCommenter().getUserId());
        commentDTO.setUsername(c.getCommenter().getUsername());
        Set<Integer> likedbyids = new HashSet<>();
        for (User u : c.getLikedBy()) {
            likedbyids.add(u.getUserId());
        }
        commentDTO.setLikedByUserIds(likedbyids);
        return commentDTO;
    
    }
    public static List<CommentDTO> CommentListToCommentDTOList(List<Comment> comments){
        List<CommentDTO> commentDTOs = new ArrayList<CommentDTO>();
        for (Comment comment : comments) {
            commentDTOs.add(CommentDTO.CommentToCommentDTO(comment));
        }
        return commentDTOs;
    }
}

