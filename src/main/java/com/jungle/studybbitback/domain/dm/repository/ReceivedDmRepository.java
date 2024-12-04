package com.jungle.studybbitback.domain.dm.repository;

import com.jungle.studybbitback.domain.dm.entity.ReceivedDm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceivedDmRepository extends JpaRepository<ReceivedDm, Long> {
	Page<ReceivedDm> findByReceiverId(Long receiverId, Pageable pageable);

	void deleteByReceiverId(Long memberId);
}
