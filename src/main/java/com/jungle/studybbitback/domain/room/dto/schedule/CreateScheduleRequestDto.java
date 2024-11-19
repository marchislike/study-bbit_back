package com.jungle.studybbitback.domain.room.dto.schedule;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateScheduleRequestDto {
    private Long roomId;
    private String title;
    private LocalDateTime scheduleDateTime;
    private String detail;

    @Builder
    public CreateScheduleRequestDto(Long roomId, String title, LocalDateTime scheduleDateTime, String detail) {
        this.roomId = roomId;
        this.title = title;
        this.scheduleDateTime = scheduleDateTime;
        this.detail = detail;
    }
}
