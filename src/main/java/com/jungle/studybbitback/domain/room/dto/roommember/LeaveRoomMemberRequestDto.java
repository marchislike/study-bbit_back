package com.jungle.studybbitback.domain.room.dto.roommember;

import lombok.Getter;

@Getter
public class LeaveRoomMemberRequestDto {
    private Long roomId;
    private Long memberId;

    public LeaveRoomMemberRequestDto(Long roomId, Long memberId) {
        this.roomId = roomId;
        this.memberId = memberId;
    }
}
