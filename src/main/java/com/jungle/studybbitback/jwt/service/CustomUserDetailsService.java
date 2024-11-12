package com.jungle.studybbitback.jwt.service;

import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import com.jungle.studybbitback.member.entity.Member;
import com.jungle.studybbitback.member.repository.MemberRepository;
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

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        //DB에서 조회
        log.info("email : {}", email);
        Member memberData = memberRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("회원을 찾을 수 없습니다.")
        );

        //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
        return new CustomUserDetails(memberData);
    }
}
