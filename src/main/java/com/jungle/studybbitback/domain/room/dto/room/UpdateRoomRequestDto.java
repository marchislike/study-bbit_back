package com.jungle.studybbitback.domain.room.dto.room;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateRoomRequestDto {
    private String detail;
    private String password;
    private String profileImageUrl;
}
