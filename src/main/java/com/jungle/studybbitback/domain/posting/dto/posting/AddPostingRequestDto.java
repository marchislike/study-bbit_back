package com.jungle.studybbitback.domain.posting.dto.posting;

import lombok.Getter;

@Getter
public class AddPostingRequestDto {
    private String title;
    private String author;

    private String content;
}
