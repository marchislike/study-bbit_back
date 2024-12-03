package com.jungle.studybbitback.notification.repository;

import com.jungle.studybbitback.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	Page<Notification> findByReceiverId(Long memberId, Pageable pageable);

	void deleteByReceiverId(Long memberId);

	@Modifying
	@Query("UPDATE Notification n SET n.isRead = true WHERE n.receiver.id = :memberId")
	void markAllAsRead(@Param("memberId") Long memberId);
}
