package com.jungle.studybbitback.domain.room.respository.roomboard;

import com.jungle.studybbitback.domain.room.entity.Room;
import com.jungle.studybbitback.domain.room.entity.roomboard.RoomBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomBoardRepository extends JpaRepository<RoomBoard, Long> {
    Page<RoomBoard> findByRoomId(Long roomId, Pageable pageable); // Page<RoomBoard> 반환
    Optional<RoomBoard> findById(Long roomBoardId); // Optional<RoomBoard> 반환
}

