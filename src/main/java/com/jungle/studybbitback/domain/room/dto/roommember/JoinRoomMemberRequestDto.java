package com.jungle.studybbitback.domain.room.dto.roommember;

import lombok.Getter;

@Getter
public class JoinRoomMemberRequestDto {
    private Long roomId;
    private String password;

    public JoinRoomMemberRequestDto(Long roomId, String password) {
        this.roomId = roomId;
        this.password = password;
    }
}
