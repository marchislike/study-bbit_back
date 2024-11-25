package com.jungle.studybbitback.notification.service;

import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import com.jungle.studybbitback.notification.controller.NotificationController;
import com.jungle.studybbitback.notification.dto.DmNotificationDto;
import com.jungle.studybbitback.notification.dto.GetNotificationResponseDto;
import com.jungle.studybbitback.notification.entity.Notification;
import com.jungle.studybbitback.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
	private final NotificationRepository notificationRepository;
/*	private static Map<Long, Integer> notificationCounts = new HashMap<>();     // 알림 개수 저장*/

	// 연결하기
	public SseEmitter subscribe(Long userId) {
		// 현재 클라이언트를 위한 sseEmitter 생성
		SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

		try {
			sseEmitter.send(SseEmitter.event().name("connect"));
			log.info("SSE 연결 완료 - userId: {}", userId);
		} catch (IOException e) {
			log.error("SSE 연결 실패 - userId: {}, 에러: {}", userId, e.getMessage(), e);
		}

		// user 의 pk 값을 key 값으로 해서 sseEmitter 를 저장
		NotificationController.sseEmitters.put(userId, sseEmitter);

		sseEmitter.onCompletion(() -> {
			log.info("SSE 연결 종료 - userId: {}", userId);
			NotificationController.sseEmitters.remove(userId);
		});

		sseEmitter.onTimeout(() -> {
			log.warn("SSE 연결 시간 초과 - userId: {}", userId);
			NotificationController.sseEmitters.remove(userId);
		});

		sseEmitter.onError((e) -> {
			log.error("SSE 연결 중 오류 - userId: {}, 에러: {}", userId, e.getMessage(), e);
			NotificationController.sseEmitters.remove(userId);
		});

		return sseEmitter;
	}

	// 쪽지 알림 - receiver 에게
	public void notifyDm(Long receiverId, Member sender, Member receiver) {
		SseEmitter sseEmitter = NotificationController.sseEmitters.get(receiverId);
		if (sseEmitter == null) {
			log.warn("SSE 연결이 존재하지 않음 - receiverId: {}", receiverId);
			return;
		}

		String content = sender.getNickname() + "(으)로부터 쪽지가 왔습니다.";
		String url = "/test-url";

		// DB 저장
		Notification notification;
		try {
			notification = new Notification(receiver, content, url);
			Notification savedNoti = notificationRepository.saveAndFlush(notification);
			log.info("알림 저장 완료 - notificationId: {}", savedNoti.getId());
		} catch (Exception ex) {
			log.error("알림 저장 실패 - receiverId: {}, 에러: {}", receiverId, ex.getMessage(), ex);
			return;
		}

		// SSE 이벤트 전송
		try {
			DmNotificationDto dto = new DmNotificationDto(content, url, notification.getCreatedAt());
			sseEmitter.send(SseEmitter.event().name("sendDm").data(dto));
			log.info("dm 알림 전송 완료 - receiverId: {}", receiverId);
		} catch (IOException e) {
			log.error("dm 알림 전송 실패 - receiverId: {}, 에러: {}", receiverId, e.getMessage(), e);
			// NotificationController.sseEmitters.remove(receiverId);
		}
	}
	// 내 알림 조회
	@Transactional(readOnly = true)
	public Page<GetNotificationResponseDto> findNotification(Pageable pageable) {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return notificationRepository.findByReceiverId(userDetails.getMemberId(), pageable).map(GetNotificationResponseDto::new);
	}

	// 알림 삭제
	@Transactional
	public String deleteNotification(Long id) {
		Notification notification = notificationRepository.findById(id).orElseThrow(
				() -> new IllegalArgumentException("알림을 찾을 수 없습니다.")
		);

		notificationRepository.delete(notification);

/*		Long userId = notification.getPost().getUser().getId();
		// 알림 개수 감소
		if (notificationCounts.containsKey(userId)) {
			int currentCount = notificationCounts.get(userId);
			if (currentCount > 0) {
				notificationCounts.put(userId, currentCount - 1);
			}
		}
		// 현재 알림 개수 전송
		SseEmitter sseEmitter = NotificationController.sseEmitters.get(userId);
		sseEmitter.send(SseEmitter.event().name("notificationCount").data(notificationCounts.get(userId)));*/

		return id + "번 알림이 삭제되었습니다.";
	}


}
