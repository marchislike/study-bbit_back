package com.jungle.studybbitback.domain.room.respository;
import com.jungle.studybbitback.domain.member.entity.Member;

import com.jungle.studybbitback.domain.room.entity.Room;
import com.jungle.studybbitback.domain.room.entity.RoomMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {

    // 스터디룸 멤버 전체 조회
    List<RoomMember> findByRoomId(Long roomId);

    // 특정 회원이 "이미" 참여했는지 확인용
    boolean existsByRoomAndMember(Room room, Member member);
    
    // 특정 방에 특정 회원이 있는지 확인용
    Optional<RoomMember> findByRoomIdAndMemberId(Long roomId, Long memberId);

    // 특정 방의 멤버수 세기
    int countByRoom(Room room);

    // Room을 기준으로 RoomMember 삭제
    void deleteByRoom(Room room);


    // 특정 Member에 속한 RoomMember를 페이지로 조회
    Page<RoomMember> findByMemberId(Long memberId, Pageable pageable);

    void deleteByRoomIdAndMemberId(Long roomId, Long banMemberId);
}
