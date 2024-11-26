package com.jungle.studybbitback.domain.dailystudy.dto;

import com.jungle.studybbitback.domain.member.entity.Member;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

@Getter
public class WriteStudyResponseDto {
	private Long memberId;
	private String nickname;
	private Map<LocalDate, Duration> studyRecord;

	public WriteStudyResponseDto(Member member, Map<LocalDate, Duration> studyRecord) {
		this.memberId = member.getId();
		this.nickname = member.getNickname();
		this.studyRecord = studyRecord;
	}
}
