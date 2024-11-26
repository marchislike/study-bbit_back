package com.jungle.studybbitback.domain.dailystudy.dto;

import com.jungle.studybbitback.domain.dailystudy.entity.DailyStudy;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDate;

@Getter
public class GetDailyStudyRequestDto {
	private LocalDate studyDate;
}
