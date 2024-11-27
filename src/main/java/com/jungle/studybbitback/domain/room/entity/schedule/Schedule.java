package com.jungle.studybbitback.domain.room.entity.schedule;

import com.jungle.studybbitback.common.entity.ModifiedTimeEntity;
import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.room.dto.schedule.CreateScheduleRequestDto;
import com.jungle.studybbitback.domain.room.entity.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.cglib.core.Local;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule extends ModifiedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Column(nullable = false)
    @NotBlank // 공백 문자열 허용하지 않음
    private String title;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;  // 일정의 시작날짜(하루 단위 날짜)

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;  // 시작 시간

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;    // 종료 시간

    @Column(length = 255)
    private String detail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private Member createdBy; // 일정 작성자

    private boolean repeatFlag; // 일정 반복 사용 여부
    private String repeatPattern; // 반복 패턴 (주간)
    private String daysOfWeek; // 반복 요일
    private LocalDate repeatEndDate; // 반복 종료 날짜

    @Column(nullable = true) // 반복일정에서 하루만 변경 시 scheduleCylcleId = null
    private Long scheduleCycleId; // 반복일정 라벨

    public void setScheduleCycleId(Long scheduleCycleId) {
        this.scheduleCycleId = scheduleCycleId;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
        this.startDate = startDateTime.toLocalDate(); // 시작 날짜 설정
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    // DTO와 Room, 작성자를 한 번에 받아 Schedule 생성
    public static Schedule from(CreateScheduleRequestDto requestDto, Room room, Member createdBy) {
        Schedule schedule = new Schedule();
        schedule.title = requestDto.getTitle();
        schedule.startDate = requestDto.getStartDate();
        schedule.startDateTime = requestDto.getStartDate().atTime(requestDto.getStartTime());
        schedule.endDateTime = requestDto.getStartDate().atTime(requestDto.getEndTime());
        schedule.detail = requestDto.getDetail();
        schedule.room = room;
        schedule.createdBy = createdBy;
        schedule.repeatFlag = requestDto.isRepeatFlag();
        schedule.repeatPattern = requestDto.getRepeatPattern();
        schedule.daysOfWeek = requestDto.getDaysOfWeek();
        schedule.repeatEndDate = requestDto.getRepeatEndDate();
        return schedule;
    }

    public void updateDetails(String title, String detail, LocalDate startDate, LocalDateTime startTime, LocalDateTime endTime,
                              Boolean repeatFlag, String repeatPattern, String daysOfWeek, LocalDate repeatEndDate) {
        if(title != null) this.title = title;
        if(detail != null) this.detail = detail;
        if(startDate != null){
            this.startDate = startDate;
            this.startDateTime = startTime;
            // endDateTime이 null이면 기본값을 설정하거나 예외를 던질 수 있습니다
            if (endTime != null) {
                this.endDateTime = endTime;
            } else {
                // 기본값을 설정하거나 예외 처리
                throw new IllegalArgumentException("종료시간은 필수로 입력되어야 합니다.");
            }
        }
        if(repeatFlag != null){
            this.repeatFlag = repeatFlag;
            this.repeatPattern = repeatPattern;
            this.daysOfWeek = daysOfWeek;
            this.repeatEndDate = repeatEndDate;
        }
    }

}
