package userData.chat.ChatRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import userData.users.User;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>{


    ChatRoom findByUserOneAndUserTwo(String userOne, String userTwo);

    ChatRoom findById(int id);
    boolean existsById(int id);

}
