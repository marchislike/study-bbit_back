package com.jungle.studybbitback.domain.room.dto.schedulemember;

import lombok.Getter;

import java.util.List;

@Getter
public class ApplyScheduleMembersRequestDto {
	private Long scheduleId;
	private List<MemberStatusDto> members;
}

