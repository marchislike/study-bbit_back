package com.jungle.studybbitback.domain.room.dto.room;

import lombok.Getter;

@Getter
public class JoinRoomRequestDto {
    private Long roomId;

    public JoinRoomRequestDto(Long roomId) {
        this.roomId = roomId;
    }
}
