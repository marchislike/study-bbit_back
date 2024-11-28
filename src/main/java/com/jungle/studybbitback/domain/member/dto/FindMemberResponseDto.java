package com.jungle.studybbitback.domain.member.dto;

import com.jungle.studybbitback.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class FindMemberResponseDto {
	private Long id;
	private String email;
	private String nickname;
	private String profileImageUrl;
	private Double flowTemperature;

	public FindMemberResponseDto(Member member) {
		this.id = member.getId();
		this.email = member.getEmail();
		this.nickname = member.getNickname();
		this.profileImageUrl = member.getProfileImageUrl();
		this.flowTemperature = member.getFlowTemperature();
	}
}
