package com.jungle.studybbitback.domain.room.respository.roomboard;

import com.jungle.studybbitback.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomBoardRepository extends JpaRepository<Room, Long> {
}
