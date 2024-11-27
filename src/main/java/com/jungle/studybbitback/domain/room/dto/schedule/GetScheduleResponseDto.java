package com.jungle.studybbitback.domain.room.dto.schedule;

import com.jungle.studybbitback.common.utils.DateUtils;
import com.jungle.studybbitback.domain.room.entity.schedule.Schedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class GetScheduleResponseDto {
    private Long scheduleId;
    private String title;
    private LocalDate startDate; // 시작 날짜
    private String day;
    private LocalTime startTime;  // 시작 시간
    private LocalTime endTime;    // 종료 시간
    private String detail;
    private Long roomId;
    private String creatorName;
    private boolean repeatFlag; // 반복 여부
    private String repeatPattern; // 반복 패턴
    private String daysOfWeek; // 반복 요일
    private String repeatEndDate; // 반복 종료 날짜
    private Long scheduleCycleId;

    public static GetScheduleResponseDto from(Schedule schedule) {
        return GetScheduleResponseDto.builder()
                .scheduleId(schedule.getId())
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .day(DateUtils.getDayInKorean(schedule.getStartDate().getDayOfWeek())) // 단일 일정 요일 추가
                .startTime(schedule.getStartDateTime().toLocalTime())
                .endTime(schedule.getEndDateTime().toLocalTime())
                .detail(schedule.getDetail())
                .roomId(schedule.getRoom().getId())
                .creatorName(schedule.getCreatedBy().getNickname())
                .repeatFlag(schedule.isRepeatFlag())
                .repeatPattern(schedule.getRepeatPattern())
                .daysOfWeek(schedule.getDaysOfWeek())
                .repeatEndDate(schedule.getRepeatEndDate() != null ? schedule.getRepeatEndDate().toString() : null)
                .scheduleCycleId(schedule.getScheduleCycleId())
                .build();
    }
}
