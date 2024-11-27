package com.jungle.studybbitback.domain.room.controller;

import com.jungle.studybbitback.domain.room.dto.banmember.BanRoomMemberRequestDto;
import com.jungle.studybbitback.domain.room.dto.banmember.BanRoomMemberResponseDto;
import com.jungle.studybbitback.domain.room.dto.banmember.GetBlacklistResponseDto;
import com.jungle.studybbitback.domain.room.dto.roommember.JoinRoomMemberRequestDto;
import com.jungle.studybbitback.domain.room.dto.roommember.JoinRoomMemberResponseDto;
import com.jungle.studybbitback.domain.room.dto.roommember.*;
import com.jungle.studybbitback.domain.room.service.RoomMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room/member")
@RequiredArgsConstructor
public class RoomMemberController {

    private final RoomMemberService roomMemberService;

    // 방 멤버 조회
    @GetMapping("/{roomId}")
    public ResponseEntity<List<GetRoomMemberResponseDto>> getRoomMembers(
            @PathVariable Long roomId) {
        List<GetRoomMemberResponseDto> response = roomMemberService.getRoomMembers(roomId);
        return ResponseEntity.ok(response);
    }

    // 방에 참여하기 (사용자가 스스로 방에 들어감)
    @PostMapping("/join/{roomId}")
    public ResponseEntity<JoinRoomMemberResponseDto> joinRoom(@PathVariable("roomId") Long roomId, @RequestBody JoinRoomMemberRequestDto requestDto) {
        JoinRoomMemberResponseDto response = roomMemberService.joinRoom(roomId, requestDto);
        return ResponseEntity.ok(response);
    }

    // 방 멤버 초대
    @PostMapping("/invite/{roomId}")
    public ResponseEntity<InviteRoomMemberResponseDto> inviteRoomMember(
            @PathVariable Long roomId, @RequestBody InviteRoomMemberRequestDto requestDto) {
        requestDto.setId(roomId);
        InviteRoomMemberResponseDto response = roomMemberService.inviteRoomMember(requestDto);
        return ResponseEntity.ok(response);
    }

    // 방 나가기
    @DeleteMapping("/leave/{roomId}")
    public ResponseEntity<String> leaveRoom(@PathVariable Long roomId) {
        String response = roomMemberService.leaveRoom(roomId);
        return ResponseEntity.ok(response);
    }

    // 방 멤버 강퇴
    @PostMapping("/ban")
    public ResponseEntity<BanRoomMemberResponseDto> banRoomMember(
            @RequestBody BanRoomMemberRequestDto request) {
        BanRoomMemberResponseDto response = roomMemberService.banRoomMember(request);
        return ResponseEntity.ok(response);
    }
    
    // 방 멤버 강퇴 취소
    @DeleteMapping("/ban")
    public ResponseEntity<BanRoomMemberResponseDto> unbanRoomMember(
            @RequestBody BanRoomMemberRequestDto request) {
        BanRoomMemberResponseDto response = roomMemberService.unbanRoomMember(request);
        return ResponseEntity.ok(response);
    }

    // 블랙리스트 조회
    @GetMapping("/ban/{roomId}")
    public ResponseEntity<Page<GetBlacklistResponseDto>> getBlacklist(
            @PathVariable("roomId") Long roomId,
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<GetBlacklistResponseDto> response = roomMemberService.getBlacklist(roomId, pageable);
        return ResponseEntity.ok(response);
    }
}