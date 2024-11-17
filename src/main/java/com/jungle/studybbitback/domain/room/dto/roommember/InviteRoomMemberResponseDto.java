package com.jungle.studybbitback.domain.room.dto.roommember;

import com.jungle.studybbitback.domain.room.entity.RoomMember;
import lombok.Getter;

@Getter
public class InviteRoomMemberResponseDto {
    private Long roomId;
    private String nickname;
    private String email;

    public InviteRoomMemberResponseDto(RoomMember roomMember) {
        this.roomId = roomMember.getRoom().getId();
        this.nickname = roomMember.getMember().getNickname();
        this.email = roomMember.getMember().getEmail();
    }
}
