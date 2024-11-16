package com.jungle.studybbitback.domain.room.dto.roommember;

import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.room.entity.Room;
import com.jungle.studybbitback.domain.room.entity.RoomMember;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetRoomMemberResponseDto {
    private Long roomId;
    private String nickname;

    public GetRoomMemberResponseDto(RoomMember roomMember) {
        this.roomId = roomMember.getRoom().getId();
        this.nickname = roomMember.getMember().getNickname();
    }
}
