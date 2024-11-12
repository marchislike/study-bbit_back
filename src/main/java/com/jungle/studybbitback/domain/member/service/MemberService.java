package com.jungle.studybbitback.domain.member.service;

import com.jungle.studybbitback.domain.member.dto.SignupRequestDto;
import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.member.entity.MemberRoleEnum;
import com.jungle.studybbitback.domain.member.repository.MemberRepository;
import com.jungle.studybbitback.jwt.JWTUtil;
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
        String email = signupRequestDto.getEmail();
        String password = signupRequestDto.getPassword();
        String nickname = signupRequestDto.getNickname();

        log.info("email : {}", email);

        if(memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        Member member = new Member(email, bCryptPasswordEncoder.encode(password), nickname, MemberRoleEnum.ROLE_USER);

        memberRepository.save(member);
    }

}
