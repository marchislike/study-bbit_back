package com.jungle.studybbitback.domain.room.dto.roommember;

import lombok.Getter;

@Getter
public class JoinRoomMemberRequestDto {
    private Long roomId;
    private Long memberId;

    public JoinRoomMemberRequestDto(Long roomId, Long memberId) {
        this.roomId = roomId;
        this.memberId = memberId;
    }
}
