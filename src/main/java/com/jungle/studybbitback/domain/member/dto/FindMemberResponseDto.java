package com.jungle.studybbitback.domain.member.dto;

import com.jungle.studybbitback.domain.dailystudy.entity.DailyStudy;
import com.jungle.studybbitback.domain.member.entity.Member;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Getter
public class FindMemberResponseDto {
	private Long id;
	private String email;
	private String nickname;
	private String profileImageUrl;
	private BigDecimal flowTemperature;
	private Duration studyTime;
	private LocalDateTime createdAt;

	public FindMemberResponseDto(Member member, DailyStudy dailyStudy) {
		this.id = member.getId();
		this.email = member.getEmail();
		this.nickname = member.getNickname();
		this.profileImageUrl = member.getProfileImageUrl();
		// 소수점 1자리로 설정, 반올림
		this.flowTemperature = member.getFlowTemperature();
		if(dailyStudy == null) {
			this.studyTime = Duration.ofHours(0);
		} else {
			this.studyTime = dailyStudy.getStudyTime();
		}
		this.createdAt = member.getCreatedAt();
	}
}
