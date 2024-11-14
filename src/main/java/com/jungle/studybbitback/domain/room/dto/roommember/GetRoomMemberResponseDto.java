package com.jungle.studybbitback.domain.room.dto.roommember;

import com.jungle.studybbitback.domain.room.entity.Room;
import com.jungle.studybbitback.domain.room.entity.RoomMember;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetRoomMemberResponseDto {
    private Long roomId;
    private Long memberId;

    public GetRoomMemberResponseDto(Long roomId, Long memberId) {
        this.roomId = roomId;
        this.memberId = memberId;
    }

    public GetRoomMemberResponseDto(RoomMember roomMember) {
        this.roomId = roomMember.getRoom().getId();
        this.memberId = roomMember.getMember().getId();
    }
}
