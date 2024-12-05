package com.jungle.studybbitback.domain.room.dto.roommember;

import com.jungle.studybbitback.domain.dailystudy.entity.DailyStudy;
import com.jungle.studybbitback.domain.room.entity.RoomMember;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Duration;

@Getter
public class GetRoomMemberResponseDto {
    private Long memberId;
    private String nickname;
    private String leaderLabel;
    private String profileImageUrl;
    private BigDecimal flowTemperature;
    private Duration studyTime;

	public GetRoomMemberResponseDto(RoomMember roomMember, DailyStudy dailyStudy, Long leaderId) {
        this.memberId = roomMember.getMember().getId();
        this.nickname = roomMember.getMember().getNickname();
        this.leaderLabel = roomMember.getMember().getId().equals(leaderId) ? "방장" : ""; // 방장이면 "방장", 아니면 빈 문자열
        this.profileImageUrl = roomMember.getMember().getProfileImageUrl(); //null이면 기본 이미지
        this.flowTemperature = roomMember.getMember().getFlowTemperature();
        if(dailyStudy==null) {
            this.studyTime = Duration.ofHours(0);
        } else {
            this.studyTime = dailyStudy.getStudyTime();
        }
    }
}
