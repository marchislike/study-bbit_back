package com.jungle.studybbitback.domain.dm.dto;

import com.jungle.studybbitback.domain.dm.entity.ReceivedDm;
import com.jungle.studybbitback.domain.dm.entity.SentDm;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetDmResponseDto {
	private Long id;

	private Long senderId;
	private Long receiverId;

	private String senderNickname;
	private String receiverNickname;

	private String senderProfileUrl;
	private String receiverProfileUrl;

	private String content;

	private LocalDateTime createdAt;

	public GetDmResponseDto(SentDm sentDm) {
		this.id = sentDm.getId();

		this.senderId = sentDm.getSender().getId();
		this.receiverId = sentDm.getReceiver().getId();

		this.senderNickname = sentDm.getSender().getNickname();
		this.receiverNickname = sentDm.getReceiver().getNickname();

		this.senderProfileUrl = sentDm.getSender().getProfileImageUrl();
		this.receiverProfileUrl = sentDm.getReceiver().getProfileImageUrl();

		this.content = sentDm.getContent();
		this.createdAt = sentDm.getCreatedAt();
	}

	public GetDmResponseDto(ReceivedDm receivedDm) {
		this.id = receivedDm.getId();

		this.senderId = receivedDm.getSender().getId();
		this.receiverId = receivedDm.getReceiver().getId();

		this.senderNickname = receivedDm.getSender().getNickname();
		this.receiverNickname = receivedDm.getReceiver().getNickname();

		this.senderProfileUrl = receivedDm.getSender().getProfileImageUrl();
		this.receiverProfileUrl = receivedDm.getReceiver().getProfileImageUrl();

		this.content = receivedDm.getContent();
		this.createdAt = receivedDm.getCreatedAt();
	}
}
