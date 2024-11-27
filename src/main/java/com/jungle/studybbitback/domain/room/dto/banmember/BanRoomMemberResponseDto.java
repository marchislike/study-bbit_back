package com.jungle.studybbitback.domain.room.dto.banmember;

import com.jungle.studybbitback.domain.room.entity.RoomBlacklist;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BanRoomMemberResponseDto {
	private Long banMemberId;
	private String banMemberNickname;
	private Long roomId;
	private String roomName;
	private String detail;

	public BanRoomMemberResponseDto(RoomBlacklist savedRoomBlacklist) {
		this.banMemberId = savedRoomBlacklist.getMember().getId();
		this.banMemberNickname = savedRoomBlacklist.getMember().getNickname();
		this.roomId = savedRoomBlacklist.getRoom().getId();
		this.roomName = savedRoomBlacklist.getRoom().getName();
		this.detail = savedRoomBlacklist.getDetail();
	}
}
