package com.jungle.studybbitback.posting.dto.comment;

import com.jungle.studybbitback.posting.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateCommentResponseDto {

    private String content;
    private LocalDateTime createdAt;

    public UpdateCommentResponseDto(Comment comment) {
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }
}
