package userData.chat;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import userData.chat.ChatRoom.ChatRoomRepository;
import userData.chat.ChatRoom.ChatRoom;
import userData.users.UserRepository;

@ServerEndpoint("/{username}/chat/{friend}")
@Component
public class Chat {

    @Autowired
    public void setChatRoomRepository(ChatRoomRepository repo) { chatRepo = repo;}
    private static ChatRoomRepository chatRepo;

    @Autowired
    public void setUserRepository(UserRepository u) { userRepo = u; }
    private static UserRepository userRepo;

    public static Map <Session, String> chatSession = new Hashtable<>();

    public static Map <String, Session> searchChat = new Hashtable<>();

    private ChatRoom chat;
    private String username;
    private String friend_username;

    // Logger for terminal output and debugging
    private final Logger logger = LoggerFactory.getLogger(Chat.class);

    /**
     * Connects the user to the websocket.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("friend") String friend_username) throws IOException {

        this.username = username;
        this.friend_username = friend_username;

        int id = 0;
        for(int i = 0; i < username.length(); i++) {
            id += username.charAt(i);
        }
        for(int i = 0; i < friend_username.length(); i++) {
            id += friend_username.charAt(i);
        }

        // Handle the case of a duplicate username
        if (searchChat.containsKey(username)) {
            session.getBasicRemote().sendText("Username already exists");
            session.close();
        } else {
            chatSession.put(session, username);
            searchChat.put(username, session);
        }

        if(!chatRepo.existsById(id)) {
            logger.info("[CHAT CREATED]" + "id: " + id);
            chat = new ChatRoom(id, username, friend_username);
            chatRepo.save(chat);
        } else {
            logger.info("[CHAT OPENED]" + "id: " + id);
            chat = chatRepo.findById(id);
            loadHistory(session, chat);
        }
    }


    /**
     * Removes the session from the HashMap when the user closes out of the session.
     * @param session
     * @throws IOException
     */
    @OnClose
    public void onClose(Session session) throws IOException {
        String username = chatSession.get(session);
        logger.info("[DISCONNECTED] " + username);

        chatSession.remove(session);
        searchChat.remove(username);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {

        // get the username from session-username mapping
        String username = chatSession.get(session);

        // do error handling here
        logger.info("[onError]" + username + ": " + throwable.getMessage());
    }


    /**
     * Connects the current session and sends the message.
     * @param session
     * @param message
     * @throws IOException
     */

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        // Grabs the username of the current session.
        String sender = chatSession.get(session);

        // Log the message to the terminal.
        logger.info("[MESSAGE] " + sender + ": " + message);

        // Send the message to the sender, so they see their own message.
        sendMessage(session, sender, message);

        // If the friend is different and connected, send them the message.
        if(!sender.equals(friend_username)) {
            Session friendSession = searchChat.get(friend_username);
            if(friendSession != null) {
                sendMessage(friendSession, sender, message);
            }
        }

        // Store the message in the chat history.
        chat.setContent(sender, message);
        chatRepo.save(chat);
    }

    /**
     * Handles the message sending, called from 'onMessage'.
     * @param message
     */
    public void sendMessage(Session session, String user, String message) throws IOException {
        String formattedMessage = user + ": " + message;
        session.getBasicRemote().sendText(formattedMessage);
    }

    /**
     * Allows previous messages to be sent to the users.
     */
    void loadHistory(Session session, ChatRoom chat) throws IOException {
        // Initialize the messages collection if it is lazy-loaded
        if (chat.getMessages() != null && Hibernate.isInitialized(chat.getMessages())) {
            for(int i = 0; i < chat.getMessages().size(); i++) {
                session.getBasicRemote().sendText(chat.getMessages().get(i).getUser() + ": " + chat.getMessages().get(i).getContent());
            }
        }
    }
}

