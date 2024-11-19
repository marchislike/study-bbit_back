package com.jungle.studybbitback.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class UpdateMemberRequestDto {
    private String password;
    private String nickname;
    private MultipartFile memberProfile;
    private boolean profileChanged;
}
