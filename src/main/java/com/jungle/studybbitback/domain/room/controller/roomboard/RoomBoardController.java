package com.jungle.studybbitback.domain.room.controller.roomboard;

import com.jungle.studybbitback.domain.room.dto.roomboard.*;
import com.jungle.studybbitback.domain.room.service.roomboard.RoomBoardService;
import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    //공지사항으로 등록
    @PostMapping("/mark/{roomBoardId}")
    public ResponseEntity<String> setNotice(
            @PathVariable Long roomBoardId,
            @RequestParam Long roomId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMemberId();

        roomBoardService.setNotice(roomBoardId, roomId, memberId);
        return ResponseEntity.ok("공지사항으로 등록된 게시글 RoomBoardID: " + roomBoardId);
    }

    //공지 해제
    @PostMapping("/unmark/{roomBoardId}")
    public ResponseEntity<String> removeNotice(
            @PathVariable Long roomBoardId,
            @RequestParam Long roomId,
            @AuthenticationPrincipal CustomUserDetails userDetails){
        Long memberId = userDetails.getMemberId();
        roomBoardService.removeNotice(roomId, roomBoardId, memberId);
        return ResponseEntity.ok("공지글에서 일반 게시글로 변경되었습니다. RoomBoardID: " + roomBoardId);
    }

}
