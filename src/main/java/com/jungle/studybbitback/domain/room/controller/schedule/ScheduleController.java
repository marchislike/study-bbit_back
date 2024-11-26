package com.jungle.studybbitback.domain.room.controller.schedule;

import com.jungle.studybbitback.domain.room.dto.schedule.CreateScheduleRequestDto;
import com.jungle.studybbitback.domain.room.dto.schedule.CreateScheduleResponseDto;
import com.jungle.studybbitback.domain.room.dto.schedule.GetScheduleDetailResponseDto;
import com.jungle.studybbitback.domain.room.dto.schedule.GetScheduleResponseDto;
import com.jungle.studybbitback.domain.room.service.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {
    private final ScheduleService scheduleService;

    // 일정 생성
    @PostMapping
    public ResponseEntity<CreateScheduleResponseDto> createSchedule(@RequestBody CreateScheduleRequestDto requestDto) {
        log.info("============전달받은 일정 생성 요청: {}", requestDto);
        CreateScheduleResponseDto response = scheduleService.createSchedule(requestDto);
        return ResponseEntity.status(201).body(response);
    }

    // 일정 전체 조회
    @GetMapping("/{roomId}")
    public ResponseEntity<Page<GetScheduleResponseDto>> getSchedules(
            @PathVariable Long roomId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            Pageable pageable) {
        // year와 month가 null인 경우 기본값 설정
        if (year == null) {
            year = LocalDate.now().getYear(); // 기본값: 현재 연도
        }
        if (month == null) {
            month = LocalDate.now().getMonthValue(); // 기본값: 현재 월
        }

        Page<GetScheduleResponseDto> schedules = scheduleService.getSchedulesByMonth(roomId, year, month, pageable);
        return ResponseEntity.ok(schedules);
    }

    //일정 상세 조회
    @GetMapping("/detail/{scheduleId}")
    public ResponseEntity<GetScheduleDetailResponseDto> getScheduleDetail(
            @PathVariable Long scheduleId
    ) {
        GetScheduleDetailResponseDto scheduleDetail = scheduleService.getScheduleDetail(scheduleId);
        return ResponseEntity.ok(scheduleDetail);

    }

}
