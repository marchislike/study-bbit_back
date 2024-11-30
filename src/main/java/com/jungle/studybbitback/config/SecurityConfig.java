package com.jungle.studybbitback.config;

import com.jungle.studybbitback.jwt.fiilter.CustomAuthenticationEntryPoint;
import com.jungle.studybbitback.jwt.fiilter.JWTFilter;
import com.jungle.studybbitback.jwt.JWTUtil;
import com.jungle.studybbitback.jwt.fiilter.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    // 밑의 LoginFilter에서 쓰기 위해서 AuthenticationManager bean을 등록한다.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        // 스프링 시큐리티 필터 체인에 CORS 정책을 설정
        // 스프링 시큐리티의 필터 체인에 의해 호출되는 경우 사용된다.
        http.cors((cors) -> cors
                .configurationSource(new CorsConfigurationSource() { // 익명 클래스 사용 : 아래에서 오버라이드 한 것을 여기서 쓴다
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {


                        CorsConfiguration configuration = new CorsConfiguration();

                        // 여러 도메인 허용
                        configuration.setAllowedOrigins(Arrays.asList(
                                "http://localhost:3000",    // 로컬 개발 환경
                                "https://studybbit.store"  // 배포된 환경
                        ));
                        configuration.setAllowedMethods(Collections.singletonList("*")); // 모든 메소드 허용
                        configuration.setAllowCredentials(true); // 쿠키와 같은 자격 증명 있는 요청이 허용되도록 한다.
                        configuration.setAllowedHeaders(Collections.singletonList("*")); // 모든 헤더 허용
                        configuration.setMaxAge(3600L);

                        // 클라이언트가 응답에서 Authorization 헤더를 사용할 수 있게 한다. 이 헤더에 JWT 토큰을 보내도록 설정한다.
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));

        // csrf 공격 방어 disable
        // JWT는 stateless여서 이 방식이 먹히지 않는다.
        http
                .csrf((auth) -> auth.disable());

        // form 로그인 방식 disable
        // JWT 방식을 쓸거다.
        http
                .formLogin((auth) -> auth.disable());

        // http basic 인증 방식 disable
        // JWT 방식을 쓸거다.
        http
                .httpBasic((auth) -> auth.disable());

        // 경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers( "/api/member/login").permitAll() // 로그임
                        .requestMatchers( "/api/member/signup").permitAll() // 회원가입
                        .requestMatchers(HttpMethod.GET,"/api/member/*").permitAll() // 회원정보 조회
                        .requestMatchers( "/api/member/isExist/{nickname}").permitAll() // 닉네임 중복 체크
                        .requestMatchers(HttpMethod.GET, "/api/room/**").permitAll() // 방 조회 등등등
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint) // 커스텀 AuthenticationEntryPoint 등록
                );

        //// 로그인 필터를 등록해준다.
        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil);
        loginFilter.setFilterProcessesUrl("/api/member/login");

        http
                .addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JWTFilter(jwtUtil), LoginFilter.class);

        // 세션설정 : stateless하게
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
