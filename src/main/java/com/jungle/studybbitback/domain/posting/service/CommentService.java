package com.jungle.studybbitback.domain.posting.service;

import com.jungle.studybbitback.domain.posting.entity.Posting;
import com.jungle.studybbitback.domain.posting.repository.PostingRepository;
import com.jungle.studybbitback.domain.posting.dto.comment.AddCommentRequestDto;
import com.jungle.studybbitback.domain.posting.dto.comment.AddCommentResponseDto;
import com.jungle.studybbitback.domain.posting.dto.comment.UpdateCommentRequestDto;
import com.jungle.studybbitback.domain.posting.dto.comment.UpdateCommentResponseDto;
import com.jungle.studybbitback.domain.posting.entity.Comment;
import com.jungle.studybbitback.domain.posting.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CommentService {

    private final PostingRepository postingRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public AddCommentResponseDto addComment(Long id, AddCommentRequestDto requestDto) {
        String loginName = SecurityContextHolder.getContext().getAuthentication().getName();

        Posting searchedPosting = postingRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물입니다.")
        );

        Comment comment = new Comment(requestDto, loginName, searchedPosting);
        Comment SavedComment = commentRepository.saveAndFlush(comment);

        return new AddCommentResponseDto(SavedComment);
    }

    @Transactional
    public UpdateCommentResponseDto updateComment(Long commentId, UpdateCommentRequestDto requestDto) {
        Comment searchedComment = commentRepository.findById(commentId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 댓글입니다.")
        );

        String SessionUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!searchedComment.getUsername().equals(SessionUsername)) {
            throw new BadCredentialsException("작성자만 삭제/수정할 수 있습니다.");
        }

        searchedComment.updateComment(requestDto);

        return new UpdateCommentResponseDto(searchedComment);
    }
    
    @Transactional
    public String deleteComment(Long commentId) {
        Comment searchedComment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다.")
        );

        String SessionUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!searchedComment.getUsername().equals(SessionUsername)) {
            throw new BadCredentialsException("작성자만 삭제/수정할 수 있습니다.");
        }

        commentRepository.deleteById(commentId);

        return "success";
    }
}
