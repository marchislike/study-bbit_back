package com.jungle.studybbitback.domain.room.dto.roommember;

import lombok.Getter;

@Getter
public class InviteRoomMemberRequestDto {
    private Long roomId;    // 방 ID
    private String email;   // 초대할 회원의 이메일

    public InviteRoomMemberRequestDto(Long roomId, String email) {
        this.roomId = roomId;
        this.email = email;
    }

    public void setId(Long roomId) {
        this.roomId = roomId;
    }
}
