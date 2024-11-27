package com.jungle.studybbitback.domain.room.dto.schedule;

import com.jungle.studybbitback.domain.room.dto.schedulemember.GetScheduleMemberResponseDto;
import com.jungle.studybbitback.domain.room.entity.schedule.Schedule;
import com.jungle.studybbitback.domain.room.entity.schedule.ScheduleMember;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetScheduleDetailResponseDto {
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
    private String repeatEndDate; // 반복 종료 날짜
    private Long scheduleCycleId;

    public static GetScheduleDetailResponseDto from(Schedule schedule) {

        return GetScheduleDetailResponseDto.builder()
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
                .scheduleCycleId(schedule.getScheduleCycleId())
                .build();
    }


}
