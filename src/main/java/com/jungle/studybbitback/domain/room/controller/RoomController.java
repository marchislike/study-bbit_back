package com.jungle.studybbitback.domain.room.controller;

import com.jungle.studybbitback.common.file.service.FileService;
import com.jungle.studybbitback.domain.room.dto.room.*;
import com.jungle.studybbitback.domain.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
@Slf4j
public class RoomController {

    private final RoomService roomService;
    private final FileService fileService;

    // 방 생성
    @PostMapping(value ="", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateRoomResponseDto> createRoom(
            @ModelAttribute CreateRoomRequestDto requestDto,
            @RequestParam(value = "roomImage", required = false) MultipartFile roomImage // 이미지 필드를 선택적으로 설정
    ) {
        log.info("Room Image provided: {}", roomImage != null ? roomImage.getOriginalFilename() : "방 이미지가 없습니다.");

        // 이미지가 없으면 null로 설정
        String roomImageUrl = null;
        if (roomImage != null && !roomImage.isEmpty()) {
            // 이미지가 있으면 업로드 처리
            roomImageUrl = fileService.uploadFile(roomImage, "image", 0L);
        }

        // 만약 roomImageUrl이 빈 문자열이라면 null로 처리
        if (StringUtils.isEmpty(roomImageUrl)) {
            roomImageUrl = null;
        }

        CreateRoomResponseDto response = roomService.createRoom(requestDto, roomImage);
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

    //검색으로 방 찾기(방 이름 or 방 설명)
    @GetMapping("/search")
    public ResponseEntity<Page<GetRoomResponseDto>> searchRooms(
            @RequestParam String keyword, Pageable pageable){
        Page<GetRoomResponseDto> rooms = roomService.searchRooms(keyword, pageable);
        return ResponseEntity.ok(rooms);
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
    @PostMapping(value = "/{roomId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UpdateRoomResponseDto> updateRoom(
            @PathVariable Long roomId,
            @ModelAttribute UpdateRoomRequestDto updateRoomRequestDto) {

        // 디버깅용 로그 출력
        log.info("Detail: {}", updateRoomRequestDto.getDetail());
        log.info("Password: {}", updateRoomRequestDto.getPassword());
        log.info("RoomImage: {}", updateRoomRequestDto.getRoomImage() != null ? updateRoomRequestDto.getRoomImage().getOriginalFilename() : "null");

        UpdateRoomResponseDto response = roomService.updateRoom(roomId, updateRoomRequestDto);
        return ResponseEntity.ok(response);
    }

    // 방 삭제
    @DeleteMapping("/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable("roomId") Long id) {
        String response = roomService.deleteRoom(id);
        return ResponseEntity.ok(response);
    }

}
