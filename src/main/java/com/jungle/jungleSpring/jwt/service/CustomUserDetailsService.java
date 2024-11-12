package com.jungle.jungleSpring.jwt.service;

import com.jungle.jungleSpring.jwt.dto.CustomUserDetails;
import com.jungle.jungleSpring.user.entity.User;
import com.jungle.jungleSpring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //DB에서 조회
        log.info("username : {}", username);
        User userData = userRepository.findByUsername(username).orElseThrow(
                () -> new NullPointerException("회원을 찾을 수 없습니다.")
        );

        //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
        return new CustomUserDetails(userData);
    }
}
