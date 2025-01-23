package program.chatRooms.chatRoomControl;

import jakarta.persistence.*;
import program.Groups.Group;
import program.Users.User;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "users_in_room",
        joinColumns = @JoinColumn(name = "roomId"),
        inverseJoinColumns = @JoinColumn(name = "userId")
    )
    private List<User> usersInRoom;

    // private Group group;

    private boolean isDirectMessage;

    // Constructors
    public ChatRoom() {
        usersInRoom = new ArrayList<User>();
    }

    public ChatRoom(List<User> users, Group g) {
        this.usersInRoom = users;
        // this.group = g;
    }

    public ChatRoom(List<User> users, boolean isDirectMessage) {
        this.usersInRoom = users;
        this.isDirectMessage = isDirectMessage;
    }

    // Getters and Setters

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public List<User> getUsersInRoom() {
        return usersInRoom;
    }

    public void setUsersInRoom(List<User> usersInRoom) {
        this.usersInRoom = usersInRoom;
    }

    public void addUserToRoom(User u) {
        usersInRoom.add(u);
    }

//    public Group getGroup() {
//        return group;
//    }
//
//    public void setGroup(Group group) {
//        this.group = group;
//    }

    public boolean isDirectMessage() {
        return isDirectMessage;
    }

    public void setDirectMessage(boolean isDirectMessage) {
        this.isDirectMessage = isDirectMessage;
    }

}
