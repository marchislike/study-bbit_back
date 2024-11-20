package com.jungle.studybbitback.domain.room.dto.schedule;

import com.jungle.studybbitback.domain.room.entity.schedule.Schedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CreateScheduleResponseDto {
    private Long scheduleId;
    private String title;
    private LocalDateTime startDateTime;  // 시작 시간
    private LocalDateTime endDateTime;    // 종료 시간
    private String detail;
    private Long roomId;
    private String creatorName;

    public static CreateScheduleResponseDto from(Schedule schedule) {
        return CreateScheduleResponseDto.builder()
                .scheduleId(schedule.getId())
                .title(schedule.getTitle())
                .startDateTime(schedule.getStartDateTime())
                .endDateTime(schedule.getEndDateTime())
                .detail(schedule.getDetail())
                .roomId(schedule.getRoom().getId())
                .creatorName(schedule.getCreatedBy().getNickname())
                .build();
    }
}
