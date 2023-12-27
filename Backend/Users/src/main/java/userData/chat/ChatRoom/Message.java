package userData.chat.ChatRoom;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Message {
    @GeneratedValue
    @Id
    private int id;
    private String user;
    private String content;

    @ManyToOne
    @JsonIgnore
    private ChatRoom chatroom;

    public Message() {}

    public Message(String user, String content) {
        this.user = user;
        this.content = content;
    }

    public String getUser() { return user; }

    public String getContent() { return content; }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}