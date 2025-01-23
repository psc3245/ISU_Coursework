package program.Comments;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import program.Posts.Post;
import program.Users.User;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;
    private String content; 
    @ManyToOne
    @JoinColumn(name = "user_id")  // Foreign key column in Comment table
    private User commenter;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    private LocalDateTime date;

    @ManyToMany
    @JoinTable(
            name = "user_likes_comment",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> likedBy = new HashSet<>();

    public Comment() {
    }
    public Comment(Integer commentId, String content, User commenter, Post post, LocalDateTime date) {
        this.commentId = commentId;
        this.content = content;
        this.commenter = commenter;
        this.post = post;
        this.date = date;
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
    public User getCommenter() {
        return commenter;
    }
    public void setCommenter(User commenter) {
        this.commenter = commenter;
    }
    public Post getPost() {
        return post;
    }
    public void setPost(Post post) {
        this.post = post;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public Set<User> getLikedBy() { return likedBy; }
    public void setLikedBy(Set<User> likedBy) { this.likedBy = likedBy; }
}
