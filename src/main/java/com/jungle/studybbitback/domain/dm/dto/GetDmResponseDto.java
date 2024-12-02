package com.jungle.studybbitback.domain.dm.dto;

import com.jungle.studybbitback.domain.dm.entity.Dm;
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

	public GetDmResponseDto(Dm dm) {
		this.id = dm.getId();

		this.senderId = dm.getSender().getId();
		this.receiverId = dm.getReceiver().getId();

		this.senderNickname = dm.getSender().getNickname();
		this.receiverNickname = dm.getReceiver().getNickname();

		this.senderProfileUrl = dm.getSender().getProfileImageUrl();
		this.receiverProfileUrl = dm.getReceiver().getProfileImageUrl();

		this.content = dm.getContent();
		this.createdAt = dm.getCreatedAt();
	}
}
