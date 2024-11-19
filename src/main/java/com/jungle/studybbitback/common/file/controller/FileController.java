package com.jungle.studybbitback.common.file.controller;

import com.jungle.studybbitback.common.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/file")
public class FileController {
    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("roomId") Long id) {
        return ResponseEntity.ok(fileService.uploadFile(file, "file", id));
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("fileUrl") String fileUrl) {
        return ResponseEntity.ok(fileService.deleteFile(fileUrl));
    }
}
