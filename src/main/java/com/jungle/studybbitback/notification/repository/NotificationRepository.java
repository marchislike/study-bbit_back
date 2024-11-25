package com.jungle.studybbitback.notification.repository;

import com.jungle.studybbitback.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	Page<Notification> findByReceiverId(Long memberId, Pageable pageable);
}
