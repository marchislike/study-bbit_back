package com.jungle.studybbitback.domain.dailystudy.repositody;

import java.time.LocalDate;

public interface DailyStudyRepositoryCustom {
	String findStudyByPeriod(Long memberId, LocalDate startDate, LocalDate endDate);
}
