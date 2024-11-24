package com.jungle.studybbitback.domain.dm.repository;

import com.jungle.studybbitback.domain.dm.entity.Dm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DmRepository  extends JpaRepository<Dm, Long> {
	Page<Dm> findBySenderId(Long senderId, Pageable pageable);
}
