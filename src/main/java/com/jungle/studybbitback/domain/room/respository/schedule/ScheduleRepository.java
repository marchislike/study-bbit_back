package com.jungle.studybbitback.domain.room.respository.schedule;


import com.jungle.studybbitback.domain.room.entity.schedule.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 특정 Room의 모든 일정 조회
    Page<Schedule> findByRoomId(Long roomId, Pageable pageable);

    // 특정 Room의 특정 달에 해당하는 일정 조회
    Page<Schedule> findByRoomIdAndStartDateTimeBetween(
            Long roomId,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

//    default Schedule findByIdOrThrow(Long scheduleId) {
//        return findById(scheduleId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 일정이 존재하지 않습니다."));
//    }

    Page<Schedule> findByScheduleCycleId(Long scheduleCycleId, Pageable pageable);
    Optional<Schedule> findFirstByScheduleCycleId(Long scheduleCycleId);

    void deleteByScheduleCycleId(Long scheduleCycleId);


    @Query("SELECT COALESCE(MAX(s.scheduleCycleId), 0) FROM Schedule s")
    Optional<Long> findMaxScheduleCycleId();
}

