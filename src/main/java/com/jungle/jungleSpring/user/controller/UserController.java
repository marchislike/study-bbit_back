package com.jungle.jungleSpring.user.controller;

import com.jungle.jungleSpring.user.dto.SignupRequestDto;
import com.jungle.jungleSpring.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public String signup(@Valid @RequestBody SignupRequestDto signupRequestDto, HttpServletResponse response) {
        userService.signup(signupRequestDto);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return "success";
    }

}
