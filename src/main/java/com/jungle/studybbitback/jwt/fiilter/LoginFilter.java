package com.jungle.studybbitback.jwt.fiilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jungle.studybbitback.jwt.JWTUtil;
import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import com.jungle.studybbitback.jwt.dto.LoginResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

// 현재 이 필터 (UsernamePasswordAuthenticationFilter)가 비활성화 되어있으므로, 이를 구현할 것이다.
// POST "/login" 요청이 올 경우, 이를 처리한다.

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 클라이언트 요청에서 username, password 추출
        String email = obtainUsername(request);
        String password = obtainPassword(request);

        if ("application/json".equals(request.getContentType())) {
            try {
                Map<String, String> loginData = objectMapper.readValue(request.getInputStream(), Map.class);
                email = loginData.get("email");
                password = loginData.get("password");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        log.info("username : {}", email);

        // 스프링 시큐리티에서 email과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);
        
        //token에 담은 검증을 위한 AuthenticationManager로 전달
        // AuthenticationManager가 일련의 과정을 통해 DB에 해당 id, password가 있는지 확인한다.
        return authenticationManager.authenticate(authToken);
    }

    // 검증해서 성공한 경우
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        // JWT를 발급한다.
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        Long memberId = customUserDetails.getMemberId();
        String email = customUserDetails.getEmail();
        String nickname = customUserDetails.getNickName();

        String role = authentication.getAuthorities().iterator().next().getAuthority();
        String token = jwtUtil.createJwt(memberId, email, role, nickname, 365 * 24 * 60 * 60 * 1000L);

        // HttpOnly Cookie 설정
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true); // XSS 방지
        cookie.setSecure(true); // HTTPS 환경에서만 사용
        cookie.setPath("/"); // 모든 경로에 대해 유효
        cookie.setMaxAge(365 * 24 * 60 * 60); // 1년 동안 유효
        response.addCookie(cookie);

        // JSON 응답 (선택적으로 클라이언트에 사용자 정보 제공)
        LoginResponseDto loginResponse = new LoginResponseDto(memberId, email, role, nickname);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(loginResponse));
    }

    // 검증에서 실패한 경우
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // 에러코드
        response.setStatus(400);
        response.setContentType("text/plain; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        if (failed != null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("이메일 또는 비밀번호가 올바르지 않습니다.");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("인증에 실패하였습니다.");
        }
    }
}
