package com.jungle.jungleSpring.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
public class SignupRequestDto {

    // 4~10자
    // 알파벳 소문자, 숫자
    @Size(min=4, max=10, message = "username은 4자 이상, 10자 이하로 입력해야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d).*$",
            message = "username은 알파벳 소문자와 숫자로 구성되어야 합니다.")
    private String username;
    
    // 8~15자
    // 알파벳 대소문자, 숫자, 특수문자
    @Size(min=8, max=15, message = "password는 8자 이상, 15자 이하로 입력해야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).*$",
            message = "password는 알파벳 대소문자, 숫자, 특수문자를 포함해야 합니다.")
    private String password;
}
