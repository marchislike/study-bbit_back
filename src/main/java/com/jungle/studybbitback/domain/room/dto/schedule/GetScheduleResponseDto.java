package com.jungle.studybbitback.domain.room.dto.schedule;

import com.jungle.studybbitback.domain.room.entity.schedule.Schedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GetScheduleResponseDto {
    private Long scheduleId;
    private String title;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String detail;
    private Long roomId;
    private String creatorName;

    public static GetScheduleResponseDto from(Schedule schedule) {
        return GetScheduleResponseDto.builder()
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
