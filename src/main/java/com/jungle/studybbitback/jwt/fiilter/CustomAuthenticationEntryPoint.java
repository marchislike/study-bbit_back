package com.jungle.studybbitback.jwt.fiilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
						 AuthenticationException authException) throws IOException {

		// HTTP 응답 상태 코드 설정
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json;charset=UTF-8");

		// 응답 내용 작성
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("timestamp", LocalDateTime.now().toString());
		errorResponse.put("status", HttpServletResponse.SC_UNAUTHORIZED);
		errorResponse.put("error", "Unauthorized");
		errorResponse.put("message", authException.getMessage());
		errorResponse.put("path", request.getServletPath());

		// JSON 응답 전송
		response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
	}
}
