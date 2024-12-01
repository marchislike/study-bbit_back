package com.jungle.studybbitback.jwt.fiilter;

import com.jungle.studybbitback.jwt.JWTUtil;
import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.member.entity.MemberRoleEnum;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    // jwt를 검증하는 필터
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
/*            // request에서 Authorization 헤더를 찾음
            String authorization = request.getHeader("Authorization");*/
            // Cookie에서 JWT 토큰 가져오기
            String token = null;
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("token".equals(cookie.getName())) {
                        token = cookie.getValue();
                    }
                }
            }

/*            // Authoriziation 헤더 검증
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                log.info("token null");
                throw new AuthenticationException("로그인이 필요합니다.") {
                };
            }

            log.info("authorization now");
            //Bearer 부분 제거 후 순수 토큰만 획득
            String token = authorization.split(" ")[1];

            // 토큰 소멸시간 검증
            if (jwtUtil.isExpired(token)) {

                log.info("token expired");
                throw new AuthenticationException("토큰이 만료되었습니다.") {
                };
            }*/

            if (token == null) {
                throw new AuthenticationException("로그인이 필요합니다.") {};
            } else if (jwtUtil.isExpired(token)){
                throw new AuthenticationException("토큰이 만료되었습니다.") {};
            }

            // 토큰에서 username과 role 획득
            Long memberId = jwtUtil.getMemberId(token);
            String email = jwtUtil.getEmail(token);
            String role = jwtUtil.getRole(token);
            String nickname = jwtUtil.getNickname(token);

            //User를 생성하여 값 set

            // 비밀번호는 토큰에 없었음
            // 필드가 비어있으면 안되니 그냥 임시로 아무거나 넣어두자
            // 어짜피 이거 안쓴다.
            Member member = new Member(memberId, email, "temppassword", nickname, MemberRoleEnum.valueOf(role));

            // UserDetails에 회원 정보 객체 담기
            CustomUserDetails customUserDetails = new CustomUserDetails(member);

            // 스프링 시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

            //세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (AuthenticationException ex) {
            log.error("Authentication failed: {}", ex.getMessage());

            // 인증 정보가 유효하지 않거나, 인증이 실패했을 때 남아 있는 컨텍스트 정보를 제거하여 잘못된 인증 정보를 계속 사용하는 것을 방지.
            SecurityContextHolder.clearContext();

            request.setAttribute("authErrorMessage", ex.getMessage());
        }

        // 다음 필터로 요청을 전달
        filterChain.doFilter(request, response);
    }
}
