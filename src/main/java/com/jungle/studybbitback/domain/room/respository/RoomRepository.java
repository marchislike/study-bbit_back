package com.jungle.studybbitback.domain.room.respository;
import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.room.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Long> {
    // 특정 이름 또는 상세 설명이 포함된 스터디룸 조회
    Page<Room> findByNameContainingOrDetailContaining(String nameKeyword, String detailKeyword, Pageable pageable);

    // RoomMember를 통해 특정 Member가 속한 Room을 조회
    Page<Room> findByRoomMembersMember(Member member, Pageable pageable);

    // 대소문자 구분 없이 검색하는 메서드 추가
    @Query("SELECT r FROM Room r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(r.detail) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Room> searchByNameAndDetailIgnoreCase(@Param("keyword") String keyword, Pageable pageable);
}
