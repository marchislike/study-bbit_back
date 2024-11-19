package com.jungle.studybbitback.domain.member.dto;

import com.jungle.studybbitback.domain.member.entity.Member;
import lombok.Getter;
@Getter
public class UpdateMemberResponseDto {
	private String nickname;
	private String memberProfileUrl;
	public UpdateMemberResponseDto(Member member) {
		this.nickname = member.getNickname();
		this.memberProfileUrl = member.getProfileImageUrl();
	}
}
