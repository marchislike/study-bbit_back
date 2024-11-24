package com.jungle.studybbitback.domain.room.controller.roomboard;

import com.jungle.studybbitback.domain.room.dto.roomboardcomment.CreateRoomBoardCommentRequestDto;
import com.jungle.studybbitback.domain.room.dto.roomboardcomment.CreateRoomBoardCommentResponseDto;
import com.jungle.studybbitback.domain.room.dto.roomboardcomment.UpdateRoomBoardCommentRequestDto;
import com.jungle.studybbitback.domain.room.dto.roomboardcomment.UpdateRoomBoardCommentResponseDto;
import com.jungle.studybbitback.domain.room.service.roomboard.RoomBoardCommentService;

import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/room-board-comment")
@RequiredArgsConstructor
public class RoomBoardCommentController {

    private final RoomBoardCommentService commentService;

    // 댓글 추가
    @PostMapping
    public ResponseEntity<CreateRoomBoardCommentResponseDto> createComment(
            @RequestBody CreateRoomBoardCommentRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();
        CreateRoomBoardCommentResponseDto responseDto = commentService.createComment(requestDto, memberId);
        return ResponseEntity.status(201).body(responseDto);
    }

    // 댓글 수정
    @PostMapping("/{roomBoardCommentId}")
    public ResponseEntity<UpdateRoomBoardCommentResponseDto> updateComment(
            @PathVariable Long roomBoardCommentId,
            @RequestBody UpdateRoomBoardCommentRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();
        UpdateRoomBoardCommentResponseDto updatedComment = commentService.updateComment(roomBoardCommentId, requestDto, memberId);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{roomBoardCommentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long roomBoardCommentId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();
        commentService.deleteComment(roomBoardCommentId, memberId);
        return ResponseEntity.noContent().build();
    }


}