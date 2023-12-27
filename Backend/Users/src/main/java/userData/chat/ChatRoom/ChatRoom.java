package userData.chat.ChatRoom;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Entity
public class ChatRoom {

    @Id
    private int id;
    private String userTwo;
    private String userOne;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "message_id")
    private List<Message> messages = new ArrayList<>();

    public ChatRoom() {}

    public ChatRoom(int id, String userOne, String userTwo) {
        this.userOne = userOne;
        this.userTwo = userTwo;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserOne() {
        return userOne;
    }

    public void setUserOne(String username) {
        this.userOne = username;
    }

    public void setUserTwo(String username) {
        this.userTwo = username;
    }

    public String getUserTwo() {
        return userTwo;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setContent(String user, String content) {
        messages.add(new Message(user, content));
    }

}

