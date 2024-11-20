package com.jungle.studybbitback.domain.room.entity.schedule;

import com.jungle.studybbitback.common.entity.ModifiedTimeEntity;
import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.room.dto.schedule.CreateScheduleRequestDto;
import com.jungle.studybbitback.domain.room.entity.Room;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor
public class Schedule extends ModifiedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Column(nullable = false)
    @NotBlank // 공백 문자열 허용하지 않음
    private String title;

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

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    // DTO와 Room, 작성자를 한 번에 받아 Schedule 생성
    public static Schedule from(CreateScheduleRequestDto requestDto, Room room, Member createdBy) {
        Schedule schedule = new Schedule();
        schedule.title = requestDto.getTitle();
        schedule.startDateTime = requestDto.getStartDateTime();
        schedule.endDateTime = requestDto.getEndDateTime();
        schedule.detail = requestDto.getDetail();
        schedule.room = room;
        schedule.createdBy = createdBy;
        return schedule;
    }

}
