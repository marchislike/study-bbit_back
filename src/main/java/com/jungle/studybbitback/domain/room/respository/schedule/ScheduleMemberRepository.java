package com.jungle.studybbitback.domain.room.respository.schedule;

import com.jungle.studybbitback.domain.room.entity.schedule.ScheduleMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleMemberRepository extends JpaRepository<ScheduleMember, Long> {
    //scheduleId로 해당 일정과 관련된 모든 멤버 조회
    List<ScheduleMember> findByScheduleId(Long scheduleId);
}
