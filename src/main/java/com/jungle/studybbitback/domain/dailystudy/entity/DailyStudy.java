package com.jungle.studybbitback.domain.dailystudy.entity;

import com.jungle.studybbitback.common.DurationToIntervalConverter;
import com.jungle.studybbitback.common.entity.ModifiedTimeEntity;
import com.jungle.studybbitback.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Type;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
public class DailyStudy extends ModifiedTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "daily_study_id")
	private Long id;

	// 공부한 날짜
	@Column(name = "study_date", nullable = false)
	private LocalDate studyDate;

	// 얼마나 공부했는지에 대한 시간
	@Convert(converter = DurationToIntervalConverter.class)
	@Column(name = "study_time", nullable = false, columnDefinition = "INTERVAL")
	@ColumnTransformer(write = "CAST(? AS INTERVAL)") // 삽입 시 CAST 적용
	private Duration studyTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	public DailyStudy(LocalDate studyDate, Duration studyTime, Member member) {
		this.studyDate = studyDate;
		this.studyTime = studyTime;
		this.member = member;
	}

	public void updateStudyTime(Duration newStudyTime) {
		this.studyTime = newStudyTime;
	}
}
