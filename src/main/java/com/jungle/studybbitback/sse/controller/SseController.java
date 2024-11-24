package com.jungle.studybbitback.sse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/sse")
public class SseController {

	private final ConcurrentHashMap<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

	// 클라이언트 연결
	@GetMapping("/subscribe/{userId}")
	public SseEmitter subscribe(@PathVariable Long userId) {
		SseEmitter emitter = new SseEmitter(0L); // 타임아웃 없음
		emitters.put(userId, emitter);

		emitter.onCompletion(() -> emitters.remove(userId));
		emitter.onTimeout(() -> emitters.remove(userId));
		emitter.onError((e) -> emitters.remove(userId));

		return emitter;
	}

	// 특정 사용자에게 알림 전송
	public void sendNotification(Long receiverId, String message) {
		SseEmitter emitter = emitters.get(receiverId);

		if (emitter != null) {
			try {
				emitter.send(SseEmitter.event()
						.name("notification")
						.data(message)
						.id("event-" + System.currentTimeMillis()));
			} catch (IOException e) {
				emitters.remove(receiverId);
			}
		}
	}
}
