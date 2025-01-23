package program.chatRooms.ChatSocket;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "messages")
public class Message {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    int messageId;

    @Column
    String content;

    @Column
    int senderUserId;

    @Column
    int chatRoomId;

    @Column
    String username;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent")
    private Date sent = new Date();

    public Message() {}

    public Message(String username, String message) {
        this.username = username;
        this.content = message;
    }

    public Message(String content, int senderUserId, int chatRoomId) {
        this.content = content;
        this.senderUserId = senderUserId;
        this.chatRoomId = chatRoomId;
    }

    // Getters and Setters

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(int senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(int chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public Date getDateSent() {
        return sent;
    }

    public void setDateSent(Date d) {
        this.sent = d;
    }

}
