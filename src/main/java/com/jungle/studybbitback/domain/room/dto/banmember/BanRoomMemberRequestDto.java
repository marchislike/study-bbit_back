package com.jungle.studybbitback.domain.room.dto.banmember;

import lombok.Getter;

@Getter
public class BanRoomMemberRequestDto {
	private Long banMemberId;
	private Long roomId;
	private String detail;
}
