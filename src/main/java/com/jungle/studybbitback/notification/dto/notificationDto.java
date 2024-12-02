package com.jungle.studybbitback.notification.dto;

import com.jungle.studybbitback.notification.entity.Notification;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class notificationDto {
	private Long notificationId;
	private String content;
	private String url;
	private LocalDateTime createdAt;

	public notificationDto(String content, String url, Notification notification) {
		this.notificationId = notification.getId();
		this.content = content;
		this.url = url;
		this.createdAt = notification.getCreatedAt();
	}
}
