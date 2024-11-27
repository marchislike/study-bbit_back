package com.jungle.studybbitback.domain.room.dto.schedule;



import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@ToString
public class UpdateScheduleRequestDto {
    private String title;
    private String detail;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean repeatFlag;
    private String repeatPattern;
    private String daysOfWeek;
    private LocalDate repeatEndDate;

    @Builder
    public UpdateScheduleRequestDto(String title, String detail, LocalDate startDate, LocalTime startTime,
                                    LocalTime endTime, boolean repeatFlag, String repeatPattern, String daysOfWeek,
                                    LocalDate repeatEndDate) {
        this.title = title;
        this.detail = detail;
        this.startDate = startDate;
        this.startTime = (startTime != null) ? startTime : LocalTime.of(0, 0);
        this.endTime = (endTime != null) ? endTime : LocalTime.of(23, 59);
        this.repeatFlag = repeatFlag;
        this.repeatPattern = repeatPattern;
        this.daysOfWeek = daysOfWeek;
        this.repeatEndDate = repeatEndDate;
    }

    // startDate와 startTime을 합쳐서 LocalDateTime 반환
    public LocalDateTime getStartDateTime() {
        return this.startDate.atTime(this.startTime);
    }

    // startDate와 endTime을 합쳐서 LocalDateTime 반환
    public LocalDateTime getEndDateTime() {
        return this.startDate.atTime(this.endTime);
    }

}
