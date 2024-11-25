package com.jungle.studybbitback.notification.dto;

import com.jungle.studybbitback.notification.entity.Notification;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetNotificationResponseDto {
	private Long id;
	private Long receiverId;
	private String receiverNickname;

	private String content;
	private String url;
	private LocalDateTime createdAt;

	public GetNotificationResponseDto(Notification noti) {
		this.id = noti.getId();
		this.receiverId = noti.getReceiver().getId();
		this.receiverNickname = noti.getReceiver().getNickname();
		this.content = noti.getContent();
		this.url = noti.getUrl();
		this.createdAt = noti.getCreatedAt();
	}
}
