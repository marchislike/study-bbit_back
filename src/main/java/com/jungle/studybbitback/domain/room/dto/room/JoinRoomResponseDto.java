package com.jungle.studybbitback.domain.room.dto.room;

import lombok.Getter;

@Getter
public class JoinRoomResponseDto {
    private String message;
    private Long roomId;
    private int participantCount;

    public JoinRoomResponseDto(Long roomId, String message, int participantCount) {
        this.roomId = roomId;
        this.message = message;
        this.participantCount = participantCount;
    }
}
