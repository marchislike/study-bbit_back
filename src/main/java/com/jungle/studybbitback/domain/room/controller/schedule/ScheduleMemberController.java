package com.jungle.studybbitback.domain.room.controller.schedule;

import com.jungle.studybbitback.domain.room.dto.schedulemember.*;
import com.jungle.studybbitback.domain.room.service.schedule.ScheduleMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule/member")
@RequiredArgsConstructor
public class ScheduleMemberController {
    private final ScheduleMemberService scheduleMemberService;

    // 일정 참여의사 등록
    @PostMapping
    public ResponseEntity<CreateScheduleMemberResponseDto> participateInSchedule(@RequestBody CreateScheduleMemberRequestDto requestDto) {
        CreateScheduleMemberResponseDto responseDto = scheduleMemberService.participateInSchedule(requestDto);
        return ResponseEntity.status(201).build();
    }

    // 일정 참석 명단 조회
    @GetMapping("/{scheduleId}")
    public ResponseEntity<List<GetScheduleMemberResponseDto>> getScheduleMembers(@PathVariable Long scheduleId) {
        List<GetScheduleMemberResponseDto> responseDto = scheduleMemberService.getScheduleMembers(scheduleId);
        return ResponseEntity.ok(responseDto);
    }

    // 일정 참여의사 변경
    @PostMapping("/update")
    public ResponseEntity<UpdateScheduleParticipationResponseDto> updateParticipation(
            @RequestBody UpdateScheduleParticipationRequestDto requestDto) {
        UpdateScheduleParticipationResponseDto responseDto = scheduleMemberService.updateParticipation(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 일정 참여의사 삭제
    @DeleteMapping
    public ResponseEntity<Void> deleteParticipation(@RequestParam Long scheduleId) {
        scheduleMemberService.deleteParticipation(scheduleId);
        return ResponseEntity.noContent().build();
    }
}

