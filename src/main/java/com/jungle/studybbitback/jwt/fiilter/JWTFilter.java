package com.jungle.studybbitback.jwt.fiilter;

import com.jungle.studybbitback.jwt.JWTUtil;
import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.member.entity.MemberRoleEnum;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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

        // request에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");

        // Authoriziation 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.info("token null");
            throw new AuthenticationException("로그인이 필요합니다.") {};
            // 다음 필터로 넘어간다.
//            filterChain.doFilter(request, response);
//
//            //조건이 해당되면 메소드 종료 (필수)
//            return;
        }

        log.info("authorization now");
        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        // 토큰 소멸시간 검증
        if (jwtUtil.isExpired(token)) {

            log.info("token expired");
            throw new AuthenticationException("토큰이 만료되었습니다.") {};
            // 다음 필터로 넘어간다.
/*            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;*/
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

        // 다음 필터로 요청을 전달
        filterChain.doFilter(request, response);
    }
}
