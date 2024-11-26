package com.jungle.studybbitback.domain.member.dto;

import com.jungle.studybbitback.domain.member.entity.Member;
import lombok.Getter;

import java.time.Duration;

@Getter
public class DailyGoalResponseDto {
	private Long memberId;
	private String nickname;
	private Duration dailyGoal;

	public DailyGoalResponseDto(Member member) {
		this.memberId = member.getId();
		this.nickname = member.getNickname();
		this.dailyGoal = member.getDailyGoal();
	}
}
