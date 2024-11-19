package com.jungle.studybbitback.domain.room.respository.schedule;

import com.jungle.studybbitback.domain.room.entity.schedule.ScheduleMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleMemberRepository extends JpaRepository<ScheduleMember, Long> {
    //scheduleId로 해당 일정과 관련된 모든 멤버 조회
    List<ScheduleMember> findByScheduleId(Long scheduleId);

    // scheduleId와 memberId로 특정 ScheduleMember 조회
    Optional<ScheduleMember> findByScheduleIdAndMemberId(Long scheduleId, Long memberId);
}
