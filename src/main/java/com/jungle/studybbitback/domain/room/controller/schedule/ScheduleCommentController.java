package com.jungle.studybbitback.domain.room.controller.schedule;

import com.jungle.studybbitback.domain.room.dto.schedulecomment.CreateScheduleCommentRequestDto;
import com.jungle.studybbitback.domain.room.dto.schedulecomment.CreateScheduleCommentResponseDto;
import com.jungle.studybbitback.domain.room.dto.schedulecomment.UpdateScheduleCommentRequestDto;
import com.jungle.studybbitback.domain.room.dto.schedulecomment.UpdateScheduleCommentResponseDto;
import com.jungle.studybbitback.domain.room.service.schedule.ScheduleCommentService;
import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule/comment")
public class ScheduleCommentController {

    private final ScheduleCommentService scheduleCommentService;

    // 댓글 생성
    @PostMapping
    public ResponseEntity<CreateScheduleCommentResponseDto> createComment(
            @RequestBody CreateScheduleCommentRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();
        CreateScheduleCommentResponseDto responseDto = scheduleCommentService.createComment(memberId, requestDto);
        return ResponseEntity.status(201).body(responseDto);
    }

    // 댓글 수정
    @PostMapping("/{scheduleCommentId}")
    public ResponseEntity<UpdateScheduleCommentResponseDto> updateComment(
            @PathVariable Long scheduleCommentId,
            @RequestBody UpdateScheduleCommentRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();
        UpdateScheduleCommentResponseDto responseDto = scheduleCommentService.updateComment(scheduleCommentId, requestDto, memberId);
        return ResponseEntity.ok(responseDto);
    }

    // 댓글 삭제
    @DeleteMapping("/{scheduleCommentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long scheduleCommentId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();
        scheduleCommentService.deleteComment(scheduleCommentId, memberId);
        return ResponseEntity.ok().build();
    }
}
