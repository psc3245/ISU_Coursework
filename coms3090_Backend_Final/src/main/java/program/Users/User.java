package program.Users;

import jakarta.persistence.*;
import program.Posts.Post;
import program.Reviews.Review;
import program.Users.listener.UserEntityListener;
import program.Comments.Comment;
import program.Groups.Group;

import java.util.HashSet;
import java.util.List;

import program.chatRooms.chatRoomControl.ChatRoom;

import java.util.ArrayList;
import java.util.Set;

@Entity
@Table(name="users")
@EntityListeners(UserEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(unique = true)
    private String username;

    private String university;

    private String email;

    private String password;
    @OneToMany(mappedBy = "groupOwner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Group> ownedGroups;
    @ManyToMany(mappedBy="members")
    private List<Group> memberGroups = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Post> posts;
    @OneToMany(mappedBy = "commenter" ,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
    @ManyToMany(mappedBy = "usersInRoom")
    private List<ChatRoom> chatRooms;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_following",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    private Set<User> following = new HashSet<>();
    @ManyToMany(mappedBy = "following", fetch = FetchType.EAGER)
    private Set<User> followers = new HashSet<>();

    @OneToMany(mappedBy = "reviewed", orphanRemoval = true)
    //other people's reviews on this user
    private List<Review> othersReviews;
    @OneToMany(mappedBy = "reviewer",cascade = CascadeType.ALL, orphanRemoval = true)
    //this user's reviews on other people
    private List<Review> reviews;

    @ManyToMany(mappedBy = "likedBy")
    private Set<Post> likedPosts = new HashSet<>();

    @ManyToMany(mappedBy = "likedBy")
    private Set<Post> likedReviews = new HashSet<>();

    @ManyToMany(mappedBy = "likedBy")
    private Set<Comment> likedComments = new HashSet<>();

    public User(String username, String university, String email, String password) {
        this.username = username;
        this.university = university;
        this.email = email;
        this.password = password;
    }

    public User() {}

    // Getters and Setters

    public int getUserId() { return userId; }

    public void setUserId(int id) { this.userId = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getUniversity() { return university; }

    public void setUniversity(String university) { this.university = university; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Group> getOwnedGroups() {
        return ownedGroups;
    }

    public void setOwnedGroups(List<Group> ownedGroups) {
        this.ownedGroups = ownedGroups;
    }

    public List<Group> getMemberGroups() {
        return memberGroups;
    }

    public void setMemberGroups(List<Group> memberGroups) {
        this.memberGroups = memberGroups;
    }

    public List<ChatRoom> getChatRooms() { return this.chatRooms; }

    public void setChatRooms(List<ChatRoom> c) { this.chatRooms = c;}

    public Set<User> getFollowing() {
        return following;
    }

    public void setFollowing(Set<User> following) {
        this.following = following;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    public void follow(User user) {
        following.add(user);
        user.getFollowers().add(this);
    }

    // Helper method to remove a user from the 'following' set
    public void unfollow(User user) {
        following.remove(user);
        user.getFollowers().remove(this);
    }

    public void addFollower(User user) {
        followers.add(user);
        user.getFollowing().add(this);
    }

    public void removeFollower(User user) {
        followers.remove(user);
        user.getFollowing().remove(this);
    }

    public Integer getFollowerCount() {
        return followers.size();
    }

    public Integer getFollowingCount() {
        return following.size();
    }

    public Set<Post> getLikedPosts() { return likedPosts; }

    public void setLikedPosts(Set<Post> likedPosts) { this.likedPosts = likedPosts; }

    public void likePost(Post p) { likedPosts.add(p); }
    public void unlikePost(Post p) { likedPosts.remove(p); }
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Review> getOthersReviews() {
        return othersReviews;
    }

    public void setOthersReviews(List<Review> othersReviews) {
        this.othersReviews = othersReviews;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }


    public Set<Comment> getLikedComments() {
        return likedComments;
    }

    public void setLikedComments(Set<Comment> likedComments) {
        this.likedComments = likedComments;
    }
}
