package com.jungle.studybbitback.domain.dailystudy.repositody;

import com.jungle.studybbitback.domain.dailystudy.entity.DailyStudy;
import com.jungle.studybbitback.domain.dm.entity.Dm;
import com.jungle.studybbitback.domain.member.entity.Member;
import com.querydsl.core.group.GroupBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyStudyRepository extends JpaRepository<DailyStudy, Long> {


	Optional<DailyStudy> findByMemberAndStudyDate(Member member, LocalDate currentDate);

	Page<DailyStudy> findByMemberId(Long memberId, Pageable pageable);

	Optional<DailyStudy> findByMemberIdAndStudyDate(Long memberId, LocalDate studyDate);
}
