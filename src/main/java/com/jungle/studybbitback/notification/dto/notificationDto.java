package com.jungle.studybbitback.notification.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class notificationDto {
	private String content;
	private String url;
	private LocalDateTime createdAt;

	public notificationDto(String content, String url, LocalDateTime createdAt) {
		this.content = content;
		this.url = url;
		this.createdAt = createdAt;
	}
}
