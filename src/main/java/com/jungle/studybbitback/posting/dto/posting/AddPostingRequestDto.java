package com.jungle.studybbitback.posting.dto.posting;

import lombok.Getter;

@Getter
public class AddPostingRequestDto {
    private String title;
    private String author;

    private String content;
    private String password;
}
