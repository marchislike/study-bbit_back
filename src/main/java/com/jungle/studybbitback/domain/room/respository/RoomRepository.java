package com.jungle.studybbitback.domain.room.respository;
import com.jungle.studybbitback.domain.room.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Page<Room> findByNameContainingOrDetailContaining(String nameKeyword, String detailKeyword, Pageable pageable);
}
