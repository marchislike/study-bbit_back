package com.jungle.studybbitback.domain.room.dto.schedule;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@ToString
public class CreateScheduleRequestDto {
    private Long roomId;
    private String title;
    private LocalDate startDate; // 시작 날짜
    private LocalTime startTime;  // 시작 시간
    private LocalTime endTime;    // 종료 시간
    private String detail;
    private boolean repeatFlag;  // 반복 여부
    private String repeatPattern;  // 반복 패턴 (예: "WEEKLY")
    private String daysOfWeek;  // 반복 요일 (예: "MON,WED")
    private LocalDate repeatEndDate;  // 반복 종료 날짜


    @Builder
    public CreateScheduleRequestDto(Long roomId, String title, LocalDate startDate, LocalTime startTime, LocalTime endTime, String detail,
                                    boolean repeatFlag, String repeatPattern, String daysOfWeek, LocalDate repeatEndDate) {
        this.roomId = roomId;
        this.title = title;
        this.startDate = startDate;
        this.startTime = (startTime != null) ? startTime : LocalTime.of(0,0);
        this.endTime = (endTime != null) ? endTime : LocalTime.of(23,59);
        this.detail = detail;
        this.repeatFlag = repeatFlag;
        this.repeatPattern = repeatPattern;
        this.daysOfWeek = daysOfWeek;
        this.repeatEndDate = repeatEndDate;
    }
}
