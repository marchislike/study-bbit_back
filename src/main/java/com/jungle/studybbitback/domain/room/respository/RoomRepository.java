package com.jungle.studybbitback.domain.room.respository;
import com.jungle.studybbitback.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
