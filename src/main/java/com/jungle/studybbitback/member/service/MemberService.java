package com.jungle.studybbitback.member.service;

import com.jungle.studybbitback.jwt.JWTUtil;
import com.jungle.studybbitback.member.entity.Member;
import com.jungle.studybbitback.member.entity.MemberRoleEnum;
import com.jungle.studybbitback.member.repository.MemberRepository;
import com.jungle.studybbitback.member.dto.SignupRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {
    private final JWTUtil jwtUtil;

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        log.info("username : {}, password : {}", username, password);

        if(memberRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        Member member = new Member(username, bCryptPasswordEncoder.encode(password), MemberRoleEnum.ROLE_USER);

        memberRepository.save(member);
    }

}
