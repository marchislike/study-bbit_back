package com.jungle.studybbitback.domain.dailystudy.repositody;

import com.jungle.studybbitback.domain.dailystudy.entity.DailyStudy;
import com.jungle.studybbitback.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyStudyRepository extends JpaRepository<DailyStudy, Long> {


	Optional<DailyStudy> findByMemberAndStudyDate(Member member, LocalDate currentDate);

	Page<DailyStudy> findByMemberId(Long memberId, Pageable pageable);

	Optional<DailyStudy> findByMemberIdAndStudyDate(Long memberId, LocalDate studyDate);

	List<DailyStudy> findByStudyDate(LocalDate now);

	@Query(value = "SELECT * FROM daily_study WHERE member_id = :memberId AND EXTRACT(YEAR FROM study_date) = :year ORDER BY study_date",
			nativeQuery = true)
	List<DailyStudy> findAllByMemberIdAndStudyYearNative(@Param("memberId") Long memberId, @Param("year") Integer year);
}
