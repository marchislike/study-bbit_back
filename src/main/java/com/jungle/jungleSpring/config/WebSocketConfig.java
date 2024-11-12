package com.jungle.jungleSpring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// https://devel-repository.tistory.com/39#google_vignette

@Configuration
@EnableWebSocketMessageBroker // WebSocket 메시징 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") //ws는 클라이언트가 WebSocket에 연결할 때 사용할 엔드포인트
                .setAllowedOriginPatterns("*") // 모든 도메인 허용
                .withSockJS();
    }
}
