package com.jungle.studybbitback.domain.room.dto.roommember;

import lombok.Getter;

@Getter
public class JoinRoomMemberResponseDto {
    private Long roomId;
    private Long memberId;
    private int participantCount;
    private String message;

    public JoinRoomMemberResponseDto(Long roomId, Long memberId, int participantCount, String message) {
        this.roomId = roomId;
        this.memberId = memberId;
        this.participantCount = participantCount;
        this.message = message;
    }
}
