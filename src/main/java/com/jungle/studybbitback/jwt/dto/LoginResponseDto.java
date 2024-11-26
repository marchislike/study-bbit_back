package com.jungle.studybbitback.jwt.dto;

import lombok.Getter;

@Getter
public class LoginResponseDto {
	private Long memberId;
	private String email;
	private String role;
	private String nickname;

	public LoginResponseDto(Long memberId, String email, String role, String nickname) {
		this.memberId = memberId;
		this.email = email;
		this.role = role;
		this.nickname = nickname;
	}
}
