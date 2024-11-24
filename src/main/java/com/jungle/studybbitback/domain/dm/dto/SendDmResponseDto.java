package com.jungle.studybbitback.domain.dm.dto;

import com.jungle.studybbitback.domain.dm.entity.Dm;
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

	public SendDmResponseDto(Dm savedDm) {
		this.senderId = savedDm.getSender().getId();
		this.receiverId = savedDm.getReceiver().getId();

		this.senderNickname = savedDm.getSender().getNickname();
		this.receiverNickname = savedDm.getReceiver().getNickname();

		this.content = savedDm.getContent();
		this.createdAt = savedDm.getCreatedAt();
	}
}
