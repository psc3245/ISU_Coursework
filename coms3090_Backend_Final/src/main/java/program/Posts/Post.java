package program.Posts;

import jakarta.persistence.*;
import org.hibernate.mapping.Join;
import program.Comments.Comment;
import program.Groups.Group;
import program.Posts.PostTypes.PostTypes;
import program.Users.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "posts")
public abstract class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    @Enumerated(EnumType.STRING) // Store the enum as a string in the database
    @Column(name = "type", insertable = false, updatable = false)
    private PostTypes type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = true)
    private Group group;

    private String title;

    private String description;

    // What data type for image
    // private Object(?) img;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments; 
    private LocalDateTime date;

    
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "user_likes_post",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> likedBy = new HashSet<>();

    public Post(Integer postId, PostTypes type, String title, String description) {
        this.postId = postId;
        this.title = title;
        this.description = description;
        this.type = type;
    }

    public Post() {}

    // Getters and Setters

    public Integer getPostId() { return postId; }

    public void setPostId(Integer id) { this.postId = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

     public LocalDateTime getDate() {
         return date;
     }

     public void setDate(LocalDateTime date) {
         this.date = date;
     }

    public PostTypes getType() {
        return type;
    }

    public void setType(PostTypes type) {
        this.type = type;
    }

    public Optional<Group> getGroup() {
        return Optional.ofNullable(group);
    }

    public void setGroup(Group g) {
        this.group = g;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Set<User> getLikedBy() { return likedBy; }

    public void setLikedBy(Set<User> likedBy) { this.likedBy = likedBy; }

    public void userLikePost(User u) { likedBy.add(u); }

    public void userUnLikePost(User u) { likedBy.remove(u); }

}

