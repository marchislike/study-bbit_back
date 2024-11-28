package com.jungle.studybbitback.domain.room.respository.schedule;

import com.jungle.studybbitback.domain.room.entity.schedule.Schedule;
import com.jungle.studybbitback.domain.room.entity.schedule.ScheduleComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleCommentRepository extends JpaRepository<ScheduleComment, Long> {
    // 특정 일정에 대한 댓글 목록 조회 (최신순)
    Page<ScheduleComment> findByScheduleOrderByCreatedAtDesc(Schedule schedule, Pageable pageable);

    // 특정 일정의 댓글 전체 삭제 (일정 삭제 시 사용)
    void deleteAllBySchedule(Schedule schedule);

    // 특정 일정의 댓글 존재 여부 확인
    boolean existsBySchedule(Schedule schedule);
}
