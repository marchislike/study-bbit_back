package com.jungle.studybbitback.domain.room.controller.schedule;

import com.jungle.studybbitback.domain.room.dto.schedule.*;
import com.jungle.studybbitback.domain.room.service.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 일별(하루) 일정 조회
    @GetMapping("/{roomId}/daily")
    public ResponseEntity<List<GetScheduleResponseDto>> getDailySchedules(
            @PathVariable Long roomId,
            @RequestParam(name = "date") String date) {
        log.info("일별 일정 조회 요청 - roomId: {}, date: {}", roomId, date);

        try {
            List<GetScheduleResponseDto> schedules = scheduleService.getDailySchedules(roomId, date);
            log.info("조회된 일정 수: {}", schedules.size());
            return ResponseEntity.ok(schedules);
        } catch (IllegalArgumentException e) {
            log.error("일정 조회 중 오류 발생: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생: {}", e.getMessage());
            throw e;
        }
    }

    // 전체 일정 조회
    @GetMapping("/{roomId}")
    public ResponseEntity<List<GetScheduleResponseDto>> getAllSchedules(
            @PathVariable Long roomId,
            @RequestParam("month") String month) {
        List<GetScheduleResponseDto> schedules = scheduleService.getAllSchedules(roomId, month);
        return ResponseEntity.ok(schedules);
    }

    //일정 상세 조회
    @GetMapping("/detail/{scheduleId}")
    public ResponseEntity<GetScheduleDetailResponseDto> getScheduleDetail(
            @PathVariable Long scheduleId,
            @PageableDefault(size = 3, sort = "createdAt", direction = Sort.Direction.ASC) Pageable commentPageable
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
            @RequestBody UpdateAllScheduleRequestDto updateAllRequestDto
    ) {
        List<UpdateAllScheduleResponseDto> responseDto = scheduleService.updateAllSchedule(scheduleCycleId, updateAllRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 반복 일정 수정 중 이후 일정들에 대해서만 수정 ***********************************
    @PostMapping("/upcoming/{scheduleCycleId}")
    public ResponseEntity<List<UpdateUpcomingScheduleResponseDto>> updateUpcomingSchedules(
            @PathVariable Long scheduleCycleId,
            @RequestBody UpdateUpcomingScheduleRequestDto requestDto) {
        List<UpdateUpcomingScheduleResponseDto> responseDto = scheduleService.updateUpcomingSchedules(scheduleCycleId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 단일 일정 삭제
    @DeleteMapping("/single/{scheduleId}")
    public ResponseEntity<String> deleteSingleSchedule(@PathVariable Long scheduleId) {
        scheduleService.deleteSingleSchedule(scheduleId);
        return ResponseEntity.ok("해당 날짜의 일정이 삭제되었습니다.");
    }

    // 반복 일정 전체 삭제
    @DeleteMapping("/all/{scheduleCycleId}")
    public ResponseEntity<String> deleteAllSchedule(@PathVariable Long scheduleCycleId) {
        scheduleService.deleteAllSchedule(scheduleCycleId);
        return ResponseEntity.ok("주간 반복 일정 전체가 삭제되었습니다.");
    }

    // 특정 시점 이후의 일정들만 삭제
    @DeleteMapping("/upcoming/{scheduleCycleId}")
    public ResponseEntity<String> deleteUpcomingSchedulesAfterDate(
            @PathVariable Long scheduleCycleId,
            @RequestBody UpdateUpcomingScheduleRequestDto requestDto) {
        scheduleService.deleteUpcomingSchedules(scheduleCycleId, requestDto);
        return ResponseEntity.ok("삭제 시작일 이후의 일정들이 삭제되었습니다.");
    }



}
