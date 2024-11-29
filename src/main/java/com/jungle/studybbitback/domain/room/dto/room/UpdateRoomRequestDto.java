package com.jungle.studybbitback.domain.room.dto.room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class UpdateRoomRequestDto {
    private String detail;
    private String password;
    private MultipartFile roomImage;
    private boolean roomImageChanged;
}
