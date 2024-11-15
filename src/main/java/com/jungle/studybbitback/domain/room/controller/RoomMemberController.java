package com.jungle.studybbitback.domain.room.controller;

import com.jungle.studybbitback.domain.room.dto.roommember.JoinRoomMemberRequestDto;
import com.jungle.studybbitback.domain.room.dto.roommember.JoinRoomMemberResponseDto;
import com.jungle.studybbitback.domain.room.dto.roommember.*;
import com.jungle.studybbitback.domain.room.service.RoomMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/room/member")
@RequiredArgsConstructor
public class RoomMemberController {

    private final RoomMemberService roomMemberService;

    // 방에 참여하기 (사용자가 스스로 방에 들어감)
    @PostMapping("/join/{roomId}")
    public ResponseEntity<JoinRoomMemberResponseDto> joinRoom(@PathVariable("roomId") Long roomId, @RequestBody JoinRoomMemberRequestDto requestDto) {
        JoinRoomMemberResponseDto response = roomMemberService.joinRoom(roomId, requestDto);
        return ResponseEntity.ok(response);
    }

    // 방 멤버 초대
    @PostMapping("/invite/{roomId}")
    public ResponseEntity<InviteRoomMemberResponseDto> inviteRoomMember(
            @PathVariable Long roomId, @RequestBody String email) {
        InviteRoomMemberRequestDto requestDto = new InviteRoomMemberRequestDto(roomId, email);
        InviteRoomMemberResponseDto response = roomMemberService.inviteRoomMember(requestDto);
        return ResponseEntity.ok(response);
    }

    // 방 멤버 조회
    @GetMapping("/{roomId}")
    public ResponseEntity<GetRoomMemberResponseDto> getRoomMember(
            @PathVariable Long roomId, @PathVariable Long memberId) {
        GetRoomMemberResponseDto response = roomMemberService.getRoomMember(roomId, memberId);
        return ResponseEntity.ok(response);
    }

    // 방 나가기
    @DeleteMapping("/leave/{roomId}")
    public ResponseEntity<String> leaveRoom(
            @PathVariable Long roomId, @PathVariable Long memberId) {
        LeaveRoomMemberRequestDto requestDto = new LeaveRoomMemberRequestDto(roomId, memberId);
        String response = roomMemberService.leaveRoom(requestDto);
        return ResponseEntity.ok(response);
    }
}