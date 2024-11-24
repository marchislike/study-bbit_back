
package com.jungle.studybbitback.domain.room.respository.roomboard;

import com.jungle.studybbitback.domain.room.entity.roomboard.RoomBoardComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomBoardCommentRepository extends JpaRepository<RoomBoardComment, Long> {
    Page<RoomBoardComment> findByRoomBoardId(Long roomBoardId, Pageable pageable);
}
