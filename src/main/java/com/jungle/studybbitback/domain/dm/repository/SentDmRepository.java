package com.jungle.studybbitback.domain.dm.repository;

import com.jungle.studybbitback.domain.dm.entity.SentDm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SentDmRepository extends JpaRepository<SentDm, Long> {
	Page<SentDm> findBySenderId(Long senderId, Pageable pageable);

	void deleteBySenderId(Long memberId);
}
