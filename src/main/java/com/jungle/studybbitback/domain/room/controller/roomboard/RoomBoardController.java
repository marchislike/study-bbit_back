package com.jungle.studybbitback.domain.room.controller.roomboard;

import com.jungle.studybbitback.domain.room.dto.roomboard.*;
import com.jungle.studybbitback.domain.room.service.roomboard.RoomBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/room-board")
@RequiredArgsConstructor
public class RoomBoardController {

    private final RoomBoardService roomBoardService;

    // 스터디룸 게시글 생성
    @PostMapping
    public ResponseEntity<CreateRoomBoardResponseDto> createRoomBoard(@RequestBody CreateRoomBoardRequestDto requestDto) {
        CreateRoomBoardResponseDto responseDto = roomBoardService.createRoomBoard(requestDto);

        return ResponseEntity.status(201).body(responseDto);
    }

    // 스터디룸 전체 게시글 조회
    @GetMapping("/{roomId}")
    public ResponseEntity<Page<GetRoomBoardResponseDto>> getRoomBoards(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<GetRoomBoardResponseDto> responseDtos = roomBoardService.getRoomBoards(roomId, pageable);
        return ResponseEntity.ok(responseDtos);

    }

    // 스터디룸 게시글 상세 조회
    @GetMapping("/detail/{roomBoardId}")
    public ResponseEntity<GetRoomBoardDetailResponseDto> getRoomBoardDetail(
            @PathVariable Long roomBoardId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) { //게시글 댓글 함께 조회
        GetRoomBoardDetailResponseDto responseDto = roomBoardService.getRoomBoardDetail(roomBoardId, page, size);
        return ResponseEntity.ok(responseDto);
    }

    // 스터디룸 게시글 수정
// 게시글 수정
    @PostMapping("/{roomBoardId}")
    public ResponseEntity<UpdateRoomBoardResponseDto> updateRoomBoard(
            @PathVariable Long roomBoardId,
            @RequestBody UpdateRoomBoardRequestDto requestDto) {
        UpdateRoomBoardResponseDto responseDto = roomBoardService.updateRoomBoard(roomBoardId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 게시글 삭제
    @DeleteMapping("/{roomBoardId}")
    public ResponseEntity<Void> deleteRoomBoard(@PathVariable Long roomBoardId) {
        roomBoardService.deleteRoomBoard(roomBoardId);
        return ResponseEntity.noContent().build();
    }

}
