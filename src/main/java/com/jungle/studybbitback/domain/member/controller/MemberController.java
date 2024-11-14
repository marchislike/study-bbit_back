package com.jungle.studybbitback.domain.member.controller;

import com.jungle.studybbitback.domain.member.dto.SignupRequestDto;
import com.jungle.studybbitback.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public String signup(@Valid @RequestBody SignupRequestDto signupRequestDto, HttpServletResponse response) {
        memberService.signup(signupRequestDto);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return "success";
    }

}
