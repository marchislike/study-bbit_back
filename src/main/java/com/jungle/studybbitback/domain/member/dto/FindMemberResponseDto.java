package com.jungle.studybbitback.domain.member.dto;

import com.jungle.studybbitback.domain.dailystudy.entity.DailyStudy;
import com.jungle.studybbitback.domain.member.entity.Member;
import lombok.Getter;

import java.time.Duration;

@Getter
public class FindMemberResponseDto {
	private Long id;
	private String email;
	private String nickname;
	private String profileImageUrl;
	private Double flowTemperature;
	private Duration studyTime;

	public FindMemberResponseDto(Member member, DailyStudy dailyStudy) {
		this.id = member.getId();
		this.email = member.getEmail();
		this.nickname = member.getNickname();
		this.profileImageUrl = member.getProfileImageUrl();
		this.flowTemperature = member.getFlowTemperature();
		if(dailyStudy == null) {
			this.studyTime = Duration.ofHours(0);
		} else {
			this.studyTime = dailyStudy.getStudyTime();
		}
	}
}
