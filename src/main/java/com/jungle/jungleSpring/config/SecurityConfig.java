package com.jungle.jungleSpring.config;

import com.jungle.jungleSpring.jwt.fiilter.JWTFilter;
import com.jungle.jungleSpring.jwt.JWTUtil;
import com.jungle.jungleSpring.jwt.fiilter.LoginFilter;
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

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

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

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000")); // 3000번대만 허용한다.
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
                                .requestMatchers(HttpMethod.POST, "/posting", "/posting/{id}").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/posting/{id}").authenticated()
                                .requestMatchers("/comment/**").authenticated()
                                .anyRequest().permitAll()
                );

        //// jwt 필터를 등록해준다.
        http
                .addFilterAt(new JWTFilter(jwtUtil), LoginFilter.class);


        //// 로그인 필터를 등록해준다.
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // 세션설정 : stateless하게
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 로그인 경로를 바꿔준다.
//        http
//                .formLogin(form -> form
//                        .loginProcessingUrl("/user/login")
//                );

        return http.build();
    }
}
