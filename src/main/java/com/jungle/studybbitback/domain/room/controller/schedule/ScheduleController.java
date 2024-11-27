package com.jungle.studybbitback.domain.room.controller.schedule;

import com.jungle.studybbitback.domain.room.dto.schedule.*;
import com.jungle.studybbitback.domain.room.service.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    // 전체 일정 조회
    @GetMapping("/{roomId}")
    public ResponseEntity<Page<GetScheduleResponseDto>> getAllSchedules(
            @PathVariable Long roomId,
            Pageable pageable) {
        Page<GetScheduleResponseDto> schedules = scheduleService.getAllSchedules(roomId, pageable);
        return ResponseEntity.ok(schedules);
    }

    //일정 상세 조회
    @GetMapping("/detail/{scheduleId}")
    public ResponseEntity<GetScheduleDetailResponseDto> getScheduleDetail(
            @PathVariable Long scheduleId,
            Pageable commentPageable
    ) {
        GetScheduleDetailResponseDto scheduleDetail = scheduleService.getScheduleDetail(scheduleId, commentPageable);
        return ResponseEntity.ok(scheduleDetail);

    }

    // 단일 일정 수정
    @PostMapping("/single/{scheduleId}")
    public ResponseEntity<UpdateScheduleResponseDto> updateSingleSchedule(
            @PathVariable Long scheduleId,
            @RequestBody UpdateScheduleRequestDto updateScheduleRequestDto) {
        UpdateScheduleResponseDto responseDto = scheduleService.updateSingleSchedule(scheduleId, updateScheduleRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 일정 전체 수정
    @PostMapping("/all/{scheduleCycleId}")
    public ResponseEntity<List<UpdateAllScheduleResponseDto>> updateAllSchedule(
            @PathVariable Long scheduleCycleId,
            @RequestBody UpdateAllScheduleRequestDto updateAllRequestDto,
            Pageable pageable) {
        List<UpdateAllScheduleResponseDto> responseDto = scheduleService.updateAllSchedule(scheduleCycleId, updateAllRequestDto, pageable);
        return ResponseEntity.ok(responseDto);
    }

}
