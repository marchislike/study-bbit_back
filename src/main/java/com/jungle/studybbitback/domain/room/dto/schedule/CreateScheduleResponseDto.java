package com.jungle.studybbitback.domain.room.dto.schedule;

import com.jungle.studybbitback.domain.room.entity.schedule.Schedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
public class CreateScheduleResponseDto {
    private Long scheduleId;
    private String title;
    private LocalDate startDate; // 시작 날짜
    private LocalTime startTime;  // 시작 시간
    private LocalTime endTime;    // 종료 시간
    private String detail;
    private Long roomId;
    private String creatorName;
    private boolean repeatFlag; // 반복 여부
    private String repeatPattern; // 반복 패턴
    private String daysOfWeek; // 반복 요일
    private String repeatEndDate; // 반복 종료 날짜 (String 형식으로 변환)

    public static CreateScheduleResponseDto from(Schedule schedule) {
        return CreateScheduleResponseDto.builder()
                .scheduleId(schedule.getId())
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .startTime(schedule.getStartDateTime().toLocalTime())
                .endTime(schedule.getEndDateTime().toLocalTime())
                .detail(schedule.getDetail())
                .roomId(schedule.getRoom().getId())
                .creatorName(schedule.getCreatedBy().getNickname())
                .repeatFlag(schedule.isRepeatFlag())
                .repeatPattern(schedule.getRepeatPattern())
                .daysOfWeek(schedule.getDaysOfWeek())
                .repeatEndDate(schedule.getRepeatEndDate() != null ? schedule.getRepeatEndDate().toString() : null)
                .build();
    }
}
