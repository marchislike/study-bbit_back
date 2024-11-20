package com.jungle.studybbitback.domain.room.dto.schedule;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateScheduleRequestDto {
    private Long roomId;
    private String title;
    private LocalDateTime startDateTime;  // 시작 시간
    private LocalDateTime endDateTime;    // 종료 시간
    private String detail;

    @Builder
    public CreateScheduleRequestDto(Long roomId, String title, LocalDateTime startDateTime, LocalDateTime endDateTime, String detail) {
        this.roomId = roomId;
        this.title = title;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.detail = detail;
    }
}
