package program.Posts.DTOs.PostDTOs;



import program.Comments.DTOs.CommentDTO;
import program.Posts.PostTypes.PostTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PostDTO {

    private Integer postId;

    private String title;

    private String description;

    private PostTypes type;

    private Integer userId;

    private String username;

    private LocalDateTime date;
    private List<CommentDTO> comments;
    private Integer likes;
    private Set<Integer> likedByUserIds = new HashSet<>();

    // Basic constructor
    public PostDTO(int postId, String title, String description) {
        this.postId = postId;
        this.title = title;
        this.description = description;
    }

    // Empty constructor for debugging
    public PostDTO() {
        this.postId = 0;
        this.title = "";
        this.description = "";
        this.userId = 0;
    }

    // Getters and Setters
    public Integer getPostId() { return postId; }

    public void setPostId(Integer id) { this.postId = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Integer getUserId() { return userId; }

    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public PostTypes getType() {
        return type;
    }

    public void setType(PostTypes type) {
        this.type = type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public Integer getLikes() { return likes; }

    public void setLikes(Integer likes) { this.likes = likes; }

    public Set<Integer> getLikedByUserIds() {
        return likedByUserIds;
    }

    public void setLikedByUserIds(Set<Integer> likedByUserIds) {
        this.likedByUserIds = likedByUserIds;
    }
}
