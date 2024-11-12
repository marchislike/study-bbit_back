package com.jungle.studybbitback.member.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    private String email;
    private String password;
    private String nickname;
}
