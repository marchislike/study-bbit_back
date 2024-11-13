package com.jungle.studybbitback.domain.room.respository;
import com.jungle.studybbitback.domain.member.entity.Member;

import com.jungle.studybbitback.domain.room.entity.Room;
import com.jungle.studybbitback.domain.room.entity.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {

    // 특정 회원이 "이미" 참여했는지 확인용
    boolean existsByRoomAndMember(Room room, Member member);
    
    // 특정 방에 특정 회원이 있는지 확인용
    Optional<RoomMember> findByRoomIdAndMemberId(Long roomId, Long memberId);

    // 특정 방의 멤버수 세기
    int countByRoom(Room room);
}
