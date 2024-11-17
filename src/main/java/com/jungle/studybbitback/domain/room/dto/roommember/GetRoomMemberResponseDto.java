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
    private String leaderLabel;

    // 기본 생성자
    public GetRoomMemberResponseDto(RoomMember roomMember) {
        this.roomId = roomMember.getRoom().getId();
        this.nickname = roomMember.getMember().getNickname();
        this.leaderLabel = ""; // 기본값
    }

    // 새로운 생성자 추가 (RoomMember와 leaderId를 매개변수로 받음)
    public GetRoomMemberResponseDto(RoomMember roomMember, Long leaderId) {
        this.roomId = roomMember.getRoom().getId();
        this.nickname = roomMember.getMember().getNickname();
        this.leaderLabel = roomMember.getMember().getId().equals(leaderId) ? "방장" : ""; // 방장이면 "방장", 아니면 빈 문자열
    }
}
