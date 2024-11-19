package com.jungle.studybbitback.domain.room.respository.schedule;


import com.jungle.studybbitback.domain.room.entity.schedule.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 특정 Room의 모든 일정 조회
    Page<Schedule> findByRoomId(Long roomId, Pageable pageable);

    // 특정 Room의 특정 달에 해당하는 일정 조회
    Page<Schedule> findByRoomIdAndScheduleDateTimeBetween(
            Long roomId,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

}
