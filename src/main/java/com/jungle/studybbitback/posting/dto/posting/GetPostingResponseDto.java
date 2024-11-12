package com.jungle.studybbitback.posting.dto.posting;

import com.jungle.studybbitback.posting.dto.comment.GetCommentResponseDto;
import com.jungle.studybbitback.posting.entity.Posting;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GetPostingResponseDto {
    private Long id;
    private String title;
    private String author;
    private String content;
    private LocalDateTime createdAt;
    private List<GetCommentResponseDto> comments;

    public GetPostingResponseDto(Posting posting) {
        this.id = posting.getId();
        this.title = posting.getTitle();
        this.author = posting.getAuthor();
        this.content = posting.getContent();
        this.createdAt = posting.getCreatedAt();
        this.comments = posting.getComments().stream()
                .map(GetCommentResponseDto::new)
                .collect(Collectors.toList());
    }
}
