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
                .allowedOrigins("http://localhost:3000"); // 3000번대의 요청만 허용한다.
    }
}
