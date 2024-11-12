package com.jungle.studybbitback.domain.posting.controller;

import com.jungle.studybbitback.domain.posting.dto.comment.AddCommentRequestDto;
import com.jungle.studybbitback.domain.posting.dto.comment.UpdateCommentRequestDto;
import com.jungle.studybbitback.domain.posting.service.CommentService;
import com.jungle.studybbitback.domain.posting.dto.comment.AddCommentResponseDto;
import com.jungle.studybbitback.domain.posting.dto.comment.UpdateCommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postingId}")
    public AddCommentResponseDto addPosting(@PathVariable("postingId") Long postingId, @RequestBody AddCommentRequestDto requestDto) {
        return commentService.addComment(postingId, requestDto);
    }

    @PostMapping("/update/{commentId}")
    public UpdateCommentResponseDto updatePosting(@PathVariable("commentId") Long commentId,
                                                  @RequestBody UpdateCommentRequestDto requestDto) {
        return commentService.updateComment(commentId, requestDto);
    }

    @DeleteMapping("/{commentId}")
    public String deletePosting(@PathVariable("commentId") Long commentId) {
        return commentService.deleteComment(commentId);
    }
}
