package com.jungle.studybbitback.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 스프링 MVC에서 CORS 요청을 전역으로 관리하기 위한 설정
// 시큐리티 필터 체인을 통하지 않는 모든 요청에서 사용된다.
@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        corsRegistry.addMapping("/**") // 모든 경로에 대해 CORS 허용을 지정한다.
                .allowedOrigins(
                        "http://localhost:3000",    // 로컬 개발 환경
                        "https://studybbit.store"  // 배포된 환경
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true); // 쿠키 및 인증 정보 포함 허용
    }
}
