package com.jungle.studybbitback.domain.room.dto.room;

import lombok.Getter;

@Getter
public class CreateRoomRequestDto {
    private String name;
    private String roomUrl;
    private String password;
    private String detail;
    private Integer maxParticipants;
    private String profileImageUrl;
}
