package com.jungle.studybbitback.domain.dm.dto;

import lombok.Getter;

@Getter
public class SendDmRequestDto {
	private Long receiverId;
	private String content;
}
