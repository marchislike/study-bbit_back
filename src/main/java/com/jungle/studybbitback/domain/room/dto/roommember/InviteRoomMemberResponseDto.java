package com.jungle.studybbitback.domain.room.dto.roommember;

import com.jungle.studybbitback.domain.room.entity.RoomMember;
import lombok.Getter;

@Getter
public class InviteRoomMemberResponseDto {
    private Long roomId;
    private Long memberId;

    public InviteRoomMemberResponseDto(RoomMember roomMember) {
        this.roomId = roomMember.getRoom().getId();
        this.memberId = roomMember.getMember().getId();
    }
}
