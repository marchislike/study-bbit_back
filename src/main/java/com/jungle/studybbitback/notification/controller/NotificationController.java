package com.jungle.studybbitback.notification.controller;

import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import com.jungle.studybbitback.notification.dto.GetNotificationResponseDto;
import com.jungle.studybbitback.notification.dto.SendMmNotiRequestDto;
import com.jungle.studybbitback.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/noti")
@RequiredArgsConstructor
public class NotificationController {
	private final NotificationService notificationService;
	public static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

	// 알림 SSE 연결
	@GetMapping("/subscribe")
	public SseEmitter subscribe() {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long userId = userDetails.getMemberId();

		return notificationService.subscribe(userId);
	}

	@PostMapping("/mm")
	public ResponseEntity<String> sendMmNotification(@RequestBody SendMmNotiRequestDto requestDto) {
		String response = notificationService.sendMmNotification(requestDto);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// 내가 받은 알림 조회
	@GetMapping()
	public ResponseEntity<Page<GetNotificationResponseDto>> findNotification(
			@PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC)
			Pageable pageable) {
		Page<GetNotificationResponseDto> response = notificationService.findNotification(pageable);
		return ResponseEntity.ok(response);
	}

	// 해당 알림 읽음처리
	@PostMapping("/{notiId}")
	public ResponseEntity<String> readNotification(@PathVariable Long notiId) {

		String response = notificationService.readNotification(notiId);
		return ResponseEntity.ok(response);
	}

	// 전체 알림 읽음처리
	@PostMapping()
	public ResponseEntity<String> readAllNotification() {

		String response = notificationService.readAllNotification();
		return ResponseEntity.ok(response);
	}

	// 알림 삭제
	@DeleteMapping("/{notiId}")
	public ResponseEntity<String> deleteNotification(@PathVariable Long notiId) {

		String response = notificationService.deleteNotification(notiId);
		return ResponseEntity.ok(response);
	}
	
	// 알림 전체 삭제
	@DeleteMapping()
	public ResponseEntity<String> deleteAllNotification() {

		String response = notificationService.deleteAllNotification();
		return ResponseEntity.ok(response);
	}
}
