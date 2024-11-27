package com.jungle.studybbitback.domain.room.respository;

import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.room.entity.Room;
import com.jungle.studybbitback.domain.room.entity.RoomBlacklist;
import com.jungle.studybbitback.domain.room.entity.RoomMember;
import com.querydsl.core.group.GroupBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomBlacklistRepository extends JpaRepository<RoomBlacklist, Long> {
	void deleteByRoomIdAndMemberId(Long roomId, Long banMemberId);

	Optional<RoomBlacklist> findByRoomIdAndMemberId(Long roomId, Long banMemberId);

	Page<RoomBlacklist> findByRoomId(Long roomId, Pageable pageable);
}
