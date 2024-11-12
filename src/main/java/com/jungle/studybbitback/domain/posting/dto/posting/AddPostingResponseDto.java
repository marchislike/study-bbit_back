package com.jungle.studybbitback.domain.posting.dto.posting;

import com.jungle.studybbitback.domain.posting.entity.Posting;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AddPostingResponseDto {
    private String title;
    private String author;
    private String content;

    private LocalDateTime createdAt;

    public AddPostingResponseDto(Posting posting) {
        this.title = posting.getTitle();
        this.author = posting.getAuthor();
        this.content = posting.getContent();
        this.createdAt = posting.getCreatedAt();
    }
}
