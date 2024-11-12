package com.jungle.jungleSpring.user.service;

import com.jungle.jungleSpring.jwt.JWTUtil;
import com.jungle.jungleSpring.user.repository.UserRepository;
import com.jungle.jungleSpring.user.dto.SignupRequestDto;
import com.jungle.jungleSpring.user.entity.User;
import com.jungle.jungleSpring.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {
    private final JWTUtil jwtUtil;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        log.info("username : {}, password : {}", username, password);

        if(userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        User user = new User(username, bCryptPasswordEncoder.encode(password), UserRoleEnum.ROLE_USER);

        userRepository.save(user);
    }

}
