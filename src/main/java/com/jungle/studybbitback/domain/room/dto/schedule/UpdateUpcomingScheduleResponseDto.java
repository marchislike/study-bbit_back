package com.jungle.studybbitback.domain.room.dto.schedule;

import com.jungle.studybbitback.common.utils.DateUtils;
import com.jungle.studybbitback.domain.room.entity.schedule.Schedule;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class UpdateUpcomingScheduleResponseDto {
    private Long scheduleId;
    private String title;
    private String detail;
    private LocalDate startDate; // 시작 날짜
    private String day;
    private LocalTime startTime;  // 시작 시간
    private LocalTime endTime;    // 종료 시간
    private boolean repeatFlag;
    private String repeatPattern;
    private String daysOfWeek;
    private LocalDate repeatEndDate;
    private Long scheduleCycleId; // 단일일정은 null로 받음 & 반복 일정에서 하루만 수정하면 역시 null로 옴

    // 추가 생성자: 전달받은 파라미터로 DTO 생성
    public UpdateUpcomingScheduleResponseDto(Long scheduleId, String title, LocalDate startDate,
                                             LocalTime startTime, LocalTime endTime) {
        this.scheduleId = scheduleId;
        this.title = title;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endTime = endTime;
        // 기본값 설정 (필요한 필드만 초기화)
        this.detail = "";  // 기본값으로 빈 문자열 설정
        this.day = DateUtils.getDayInKorean(startDate.getDayOfWeek());  // 요일 계산
        this.repeatFlag = false; // 기본값
        this.repeatPattern = ""; // 기본값
        this.daysOfWeek = ""; // 기본값
        this.repeatEndDate = null; // 기본값
        this.scheduleCycleId = null; // 기본값
    }
}
