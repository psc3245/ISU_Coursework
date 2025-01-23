package program.chatRooms.chatRoomControl;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
    Optional<ChatRoom> findRoomByRoomId(int roomId);
}
