package com.jungle.studybbitback.jwt.dto;

import lombok.Getter;

@Getter
public class LoginResponseDto {
	private Long memberId;
	private String email;
	private String role;
	private String nickName;

	public LoginResponseDto(Long memberId, String email, String role, String nickName) {
		this.memberId = memberId;
		this.email = email;
		this.role = role;
		this.nickName = nickName;
	}
}
