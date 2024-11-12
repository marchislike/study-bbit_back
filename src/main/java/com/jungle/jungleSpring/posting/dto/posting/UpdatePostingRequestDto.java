package com.jungle.jungleSpring.posting.dto.posting;

import lombok.Getter;

@Getter
public class UpdatePostingRequestDto {
    private String title;
    private String content;
    private String author;

    private String password;
}
