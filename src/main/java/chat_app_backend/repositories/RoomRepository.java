package chat_app_backend.repositories;

import chat_app_backend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    //tim room theo roomId
    Optional<Room> findByRoomId(String roomId);
}
