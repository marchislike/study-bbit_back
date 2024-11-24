package com.jungle.studybbitback.domain.room.service.roomboard;

import com.jungle.studybbitback.domain.member.repository.MemberRepository;
import com.jungle.studybbitback.domain.room.dto.roomboardcomment.CreateRoomBoardCommentRequestDto;
import com.jungle.studybbitback.domain.room.dto.roomboardcomment.CreateRoomBoardCommentResponseDto;
import com.jungle.studybbitback.domain.room.dto.roomboardcomment.UpdateRoomBoardCommentRequestDto;
import com.jungle.studybbitback.domain.room.dto.roomboardcomment.UpdateRoomBoardCommentResponseDto;
import com.jungle.studybbitback.domain.room.entity.roomboard.RoomBoard;
import com.jungle.studybbitback.domain.room.entity.roomboard.RoomBoardComment;
import com.jungle.studybbitback.domain.room.respository.roomboard.RoomBoardCommentRepository;
import com.jungle.studybbitback.domain.room.respository.roomboard.RoomBoardRepository;
import com.jungle.studybbitback.domain.member.entity.Member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomBoardCommentService {

    private final RoomBoardCommentRepository commentRepository;
    private final RoomBoardRepository roomBoardRepository;
    private final MemberRepository memberRepository;

    // 댓글 작성
    @Transactional
    public CreateRoomBoardCommentResponseDto createComment(CreateRoomBoardCommentRequestDto requestDto, Long memberId) {
        Long roomBoardId = requestDto.getRoomBoardId();

        RoomBoard roomBoard = roomBoardRepository.findById(roomBoardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        String content = requestDto.getContent();

        // 댓글 엔티티 생성
        RoomBoardComment comment = RoomBoardComment.builder()
                .content(content)
                .createdBy(memberId)
                .roomBoard(roomBoard)
                .build();

        RoomBoardComment savedComment = commentRepository.save(comment);

        String nickname = memberRepository.findById(memberId)
                .map(Member::getNickname)
                .orElse("Unknown");

        return new CreateRoomBoardCommentResponseDto(
                savedComment.getId(),
                savedComment.getContent(),
                nickname,
                savedComment.getCreatedAt(),
                roomBoardId);
    }

    // 댓글 수정
    @Transactional
    public UpdateRoomBoardCommentResponseDto updateComment(
            Long commentId, UpdateRoomBoardCommentRequestDto requestDto, Long memberId
    ) {
        RoomBoardComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        // 작성자 검증
        if (!comment.getCreatedBy().equals(memberId)) {
            throw new IllegalAccessError("댓글 수정 권한이 없습니다.");
        }

        String newContent = requestDto.getContent();

        comment.updateComment(newContent);
        commentRepository.save(comment);


        String nickname = memberRepository.findById(comment.getCreatedBy())
                .map(Member::getNickname)
                .orElse("Unknown");

        return new UpdateRoomBoardCommentResponseDto(
                comment.getId(),
                comment.getContent(),
                nickname,
                comment.getModifiedAt(),
                comment.getRoomBoard().getId());
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        RoomBoardComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        // 작성자 검증
        if (!comment.getCreatedBy().equals(memberId)) {
            throw new IllegalAccessError("댓글 삭제 권한이 없습니다.");
        }

        commentRepository.deleteById(commentId);
    }

}

