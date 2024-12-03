package com.jungle.studybbitback.domain.room.respository.schedule;


import com.jungle.studybbitback.domain.room.entity.schedule.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByRoomIdAndStartDateBetween(Long roomId, LocalDate startDate, LocalDate endDate);

    List<Schedule> findByScheduleCycleId(Long scheduleCycleId);

    Optional<Schedule> findFirstByScheduleCycleId(Long scheduleCycleId);

    void deleteByScheduleCycleId(Long scheduleCycleId);


    @Query("SELECT COALESCE(MAX(s.scheduleCycleId), 0) FROM Schedule s")
    Optional<Long> findMaxScheduleCycleId();

    // 주어진 연도에 해당 방에서 일정이 있는 월들을 조회하는 메서드
    @Query("SELECT DISTINCT MONTH(s.startDate) FROM Schedule s WHERE s.room.id = :roomId AND YEAR(s.startDate) = :year")
    List<Integer> findMonthsWithSchedulesByRoomIdAndYear(@Param("roomId") Long roomId, @Param("year") int year);

    // 주어진 연도와 월에 해당하는 일정을 조회하는 메서드
    @Query("FROM Schedule s WHERE s.room.id = :roomId AND YEAR(s.startDate) = :year AND MONTH(s.startDate) = :month")
    List<Schedule> findSchedulesByRoomIdAndYearAndMonth(@Param("roomId") Long roomId, @Param("year") int year, @Param("month") int month);
}

