package com.jungle.studybbitback.domain.room.dto.schedule;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
//@ToString
@NoArgsConstructor
public class UpdateUpcomingScheduleRequestDto {
    private String title;
    private String detail;
    private LocalDate startDate;
    private String day;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean repeatFlag;
    private String repeatPattern;
    private String daysOfWeek;
    private LocalDate repeatEndDate;

}
