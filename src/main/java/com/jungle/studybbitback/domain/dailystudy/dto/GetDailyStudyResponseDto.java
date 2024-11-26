package com.jungle.studybbitback.domain.dailystudy.dto;

import com.jungle.studybbitback.domain.dailystudy.entity.DailyStudy;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDate;
@Getter
public class GetDailyStudyResponseDto {
	private LocalDate studyDate;
	private Duration studyTime;

	public GetDailyStudyResponseDto(DailyStudy dailyStudy) {
		this.studyDate = dailyStudy.getStudyDate();
		this.studyTime = dailyStudy.getStudyTime();
	}

	public GetDailyStudyResponseDto(LocalDate studyDate) {
		this.studyDate = studyDate;
		this.studyTime = Duration.ofHours(0);
	}
}
