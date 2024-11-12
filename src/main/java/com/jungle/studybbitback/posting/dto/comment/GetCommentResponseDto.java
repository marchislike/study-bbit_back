package com.jungle.studybbitback.posting.dto.comment;

import com.jungle.studybbitback.posting.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetCommentResponseDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;

    public GetCommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }
}
