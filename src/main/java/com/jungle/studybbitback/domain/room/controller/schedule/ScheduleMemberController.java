package com.jungle.studybbitback.domain.room.controller.schedule;

import com.jungle.studybbitback.domain.room.dto.schedulemember.*;
import com.jungle.studybbitback.domain.room.service.schedule.ScheduleMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule/member")
@RequiredArgsConstructor
public class ScheduleMemberController {
    private final ScheduleMemberService scheduleMemberService;

    // 결석 등록
    @PostMapping("/pre-absence")
    public ResponseEntity<ApplyNotedScheduleMemberResponseDto> applyPreAbsenceScheduleMember(
            @RequestBody ApplyNotedScheduleMemberRequestDto requestDto) {
        ApplyNotedScheduleMemberResponseDto responseDto = scheduleMemberService.applyPreAbsenceScheduleMember(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    
    // 결석 취소
    @DeleteMapping("/pre-absence/{scheduleId}")
    public ResponseEntity<String> cancelPreAbsenceScheduleMember(@PathVariable("scheduleId") Long scheduleId) {
        String response = scheduleMemberService.cancelPreAbsenceScheduleMember(scheduleId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 출석부 등록
    @PostMapping()
    public ResponseEntity<List<ApplyScheduleMembersResponseDto>> applyScheduleMembers(
            @RequestBody ApplyScheduleMembersRequestDto requestDto) {
        List<ApplyScheduleMembersResponseDto> responseDtoList = scheduleMemberService.applyScheduleMembers(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDtoList);
    }
    
    // 일정 참여자 조회
        // -> 결석자 조회하거나
        // -> 출석부 등록 후 전체 멤버 조회
    @GetMapping("/{scheduleId}")
    public ResponseEntity<Page<GetScheduleMemberResponseDto>> getScheduleMembers(
            @PathVariable("scheduleId") Long scheduleId,
            @PageableDefault(size = 4, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<GetScheduleMemberResponseDto> responseDto = scheduleMemberService.getScheduleMembers(scheduleId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    
/*    // 일정 참여자 출석 등록

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
    }*/
}

