package com.jungle.studybbitback.domain.room.dto.banmember;

import com.jungle.studybbitback.domain.room.entity.RoomBlacklist;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetBlacklistResponseDto {
	private Long banMemberId;
	private String banMemberNickname;
	private String detail;
	private LocalDateTime createdAt;

	public GetBlacklistResponseDto(RoomBlacklist roomBlacklist) {
		this.banMemberId = roomBlacklist.getMember().getId();
		this.banMemberNickname = roomBlacklist.getMember().getNickname();
		this.detail = roomBlacklist.getDetail();
		this.createdAt = roomBlacklist.getCreatedAt();
	}
}
