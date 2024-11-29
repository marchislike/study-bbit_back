package com.jungle.studybbitback.domain.room.dto.roommember;

import com.jungle.studybbitback.domain.room.entity.RoomMember;
import lombok.Getter;

@Getter
public class GetRoomMemberResponseDto {
    private Long memberId;
    private String nickname;
    private String leaderLabel;
    private String profileImageUrl;
    private Double flowTemperature;

    // 새로운 생성자 추가 (RoomMember와 leaderId를 매개변수로 받음)
    public GetRoomMemberResponseDto(RoomMember roomMember, Long leaderId) {
        this.memberId = roomMember.getMember().getId();
        this.nickname = roomMember.getMember().getNickname();
        this.leaderLabel = roomMember.getMember().getId().equals(leaderId) ? "방장" : ""; // 방장이면 "방장", 아니면 빈 문자열
        this.profileImageUrl = roomMember.getMember().getProfileImageUrl(); //null이면 기본 이미지
        this.flowTemperature = roomMember.getMember().getFlowTemperature();
    }
}
