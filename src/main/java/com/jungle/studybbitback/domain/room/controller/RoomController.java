package com.jungle.studybbitback.domain.room.controller;

import com.jungle.studybbitback.domain.room.dto.room.*;
import com.jungle.studybbitback.domain.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    // 방 생성
    @PostMapping
    public ResponseEntity<CreateRoomResponseDto> createRoom(@RequestBody CreateRoomRequestDto requestDto) {
        CreateRoomResponseDto response = roomService.createRoom(requestDto);
        return ResponseEntity.ok(response);
    }

    // 모든 방 조회 (메인에서 보이는 전체 스터디룸 목록)
    @GetMapping
    public ResponseEntity<Page<GetRoomResponseDto>> getRoomAll(
           @RequestParam(defaultValue = "0") int page,
           @RequestParam(defaultValue = "8") int size) {
        Page<GetRoomResponseDto> rooms = roomService.getRoomAll(page, size);
        return ResponseEntity.ok(rooms);
    }

    // 특정 방 조회
    @GetMapping("/{roomId}")
    public ResponseEntity<GetRoomResponseDto> getRoomById(@PathVariable("roomId") Long id) {
        GetRoomResponseDto room = roomService.getRoomById(id);
        return ResponseEntity.ok(room);
    }

    // 상세 방 설명
    @GetMapping("/detail/{roomId}")
    public ResponseEntity<GetRoomDetailResponseDto> getRoomDetail(@PathVariable("roomId") Long id) {
        GetRoomDetailResponseDto roomDetail = roomService.getRoomDetail(id);
        return ResponseEntity.ok(roomDetail);
    }

    // 스터디룸 대시보드 홈 조회
    @GetMapping("/dashboard/{roomId}")
    public ResponseEntity<GetRoomDashboardResponseDto> getRoomDashboard(@PathVariable("roomId") Long id) {
        GetRoomDashboardResponseDto roomDashboard = roomService.getRoomDashboard(id);
        return ResponseEntity.ok(roomDashboard);
    }

    // 방 수정
    @PostMapping("/{roomId}")
    public ResponseEntity<UpdateRoomResponseDto> updateRoom(@PathVariable("roomId") Long id, @RequestBody UpdateRoomRequestDto requestDto) {
        UpdateRoomResponseDto response = roomService.updateRoom(id, requestDto);
        return ResponseEntity.ok(response);
    }

    // 방 삭제
    @DeleteMapping("/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable("roomId") Long id) {
        String response = roomService.deleteRoom(id);
        return ResponseEntity.ok(response);
    }
    
    //화상회의 시작, 종료, 참여 아래로 구현 예정
}
