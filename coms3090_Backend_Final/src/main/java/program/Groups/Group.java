package program.Groups;
import jakarta.persistence.*;
import program.Posts.Post;
import program.Users.User;
import java.util.ArrayList;
import java.util.List;



@Entity 
@Table(name = "Groups")
public class Group{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int groupId;

    @Column(unique = true)
    private String groupName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false)
    private User groupOwner;

    private String groupDescription;
    private String groupType;
    private int imageID;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
    name = "group_members",
    joinColumns = @JoinColumn(name = "group_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> members = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    public Group(String groupName,User groupOwner){
        this.groupName = groupName;
        this.groupOwner = groupOwner;
    }

    public Group(){}

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public User getGroupOwner() {
        return groupOwner;
    }

    public void setGroupOwner(User groupOwner) {
        this.groupOwner = groupOwner;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public List<User> getMembers() {
        return members;
    }

    public void appendMember(User member){
        this.members.add(member);
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void addPostToGroup(Post p) {
        posts.add(p);
    }

    public void removePostFromGroup(Post p) {
        posts.remove(p);
    }
}