package com.jungle.studybbitback.domain.dailystudy.repositody;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDate;

public class DailyStudyRepositoryImpl implements DailyStudyRepositoryCustom{

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public String findStudyByPeriod(Long memberId, LocalDate startDate, LocalDate endDate) {
		String sql = "WITH date_series AS ( " +
				"    SELECT generate_series( " + // PostgreSQL에서 연속된 값을 생성하는 함수
				"        CAST(:startDate AS date), " +
				"        CAST(:endDate AS date), " +
				"        INTERVAL '1 day' " +
				"    ) AS study_date " +
				") " +
				"SELECT AVG(COALESCE(ds.study_time, '0 hours'))::text AS avg_study_time " + // coalesce() : 없으면 0 hours
				"FROM date_series d " +
				"LEFT JOIN daily_study ds " +
				"ON d.study_date = ds.study_date " +
				"AND ds.member_id = :memberId";

		return entityManager.createNativeQuery(sql)
				.setParameter("memberId", memberId)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getSingleResult()
				.toString();
	}
}
