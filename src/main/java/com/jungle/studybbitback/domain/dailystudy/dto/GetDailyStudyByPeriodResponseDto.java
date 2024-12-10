package com.jungle.studybbitback.domain.dailystudy.dto;

import lombok.Getter;

import java.time.Duration;

@Getter
public class GetDailyStudyByPeriodResponseDto {
	private Duration studyTimeByPeriod;

	public GetDailyStudyByPeriodResponseDto(Duration studyTimeByPeriod) {
		this.studyTimeByPeriod = studyTimeByPeriod;
	}
}
