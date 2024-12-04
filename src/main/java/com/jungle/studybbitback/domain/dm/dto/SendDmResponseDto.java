package com.jungle.studybbitback.domain.dm.dto;

import com.jungle.studybbitback.domain.dm.entity.SentDm;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SendDmResponseDto {
	private Long senderId;
	private Long receiverId;

	private String senderNickname;
	private String receiverNickname;

	private String content;
	private LocalDateTime createdAt;

	public SendDmResponseDto(SentDm savedSentDm) {
		this.senderId = savedSentDm.getSender().getId();
		this.receiverId = savedSentDm.getReceiver().getId();

		this.senderNickname = savedSentDm.getSender().getNickname();
		this.receiverNickname = savedSentDm.getReceiver().getNickname();

		this.content = savedSentDm.getContent();
		this.createdAt = savedSentDm.getCreatedAt();
	}
}
