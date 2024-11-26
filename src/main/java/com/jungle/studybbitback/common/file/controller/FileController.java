package com.jungle.studybbitback.common.file.controller;

import com.jungle.studybbitback.common.file.dto.GetRoomFileResponseDto;
import com.jungle.studybbitback.common.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/file")
public class FileController {
    private final FileService fileService;

    @PostMapping()
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("roomId") Long id) {
        return ResponseEntity.ok(fileService.uploadFile(file, "file", id));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Page<GetRoomFileResponseDto>> getRoomFile(
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable,
            @PathVariable("roomId") Long roomId) {
        Page<GetRoomFileResponseDto> response = fileService.getRoomFile(pageable, roomId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteFile(@RequestParam("fileUrl") String fileUrl) {

        return ResponseEntity.ok(fileService.deleteUserFile(fileUrl));
    }
}