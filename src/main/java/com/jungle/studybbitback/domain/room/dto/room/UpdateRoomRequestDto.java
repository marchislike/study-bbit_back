package com.jungle.studybbitback.domain.room.dto.room;

import lombok.Getter;

@Getter
public class UpdateRoomRequestDto {
    private String detail;
    private String password;
    private String profileImageUrl;
}
