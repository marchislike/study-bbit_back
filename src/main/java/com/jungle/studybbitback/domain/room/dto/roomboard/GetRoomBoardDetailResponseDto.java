package com.jungle.studybbitback.domain.room.dto.roomboard;

import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.member.repository.MemberRepository;
import com.jungle.studybbitback.domain.room.dto.roomboardcomment.GetRoomBoardCommentResponseDto;
import com.jungle.studybbitback.domain.room.entity.roomboard.RoomBoard;
import com.jungle.studybbitback.domain.room.entity.roomboard.RoomBoardComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetRoomBoardDetailResponseDto {
    private Long roomBoardId;
    private String title;
    private String content;
    private String createdBy; // 게시글 작성자 닉네임
    private String createdByProfileUrl; // 게시글 작성자 프로필 이미지 URL
    private Long roomId;
    private LocalDateTime createdAt;
    private Page<GetRoomBoardCommentResponseDto> comments; // 게시글에 대한 댓글 리스트

    public static GetRoomBoardDetailResponseDto from(RoomBoard roomBoard, Page<RoomBoardComment> comments, MemberRepository memberRepository, Pageable pageable) {
        // 게시글 작성자 정보를 가져옴
        Member roomBoardCreator = memberRepository.findById(roomBoard.getCreatedBy())
                .orElseThrow(() -> new IllegalArgumentException("게시글 작성자 정보가 존재하지 않습니다."));

        Page<GetRoomBoardCommentResponseDto> commentDtos = comments.map(comment -> {
            Member commentCreator = memberRepository.findById(comment.getCreatedBy())
                    .orElseThrow(() -> new IllegalArgumentException("댓글 작성자가 존재하지 않습니다."));
            return new GetRoomBoardCommentResponseDto(
                    comment.getId(),
                    comment.getContent(),
                    commentCreator.getNickname(),
                    commentCreator.getProfileImageUrl(), // 프로필 이미지 URL을 포함
                    comment.getCreatedAt(),
                    roomBoard.getId()
            );
        });

        return new GetRoomBoardDetailResponseDto(
                roomBoard.getId(),
                roomBoard.getTitle(),
                roomBoard.getContent(),
                roomBoardCreator.getNickname(), // 게시글 작성자 닉네임
                roomBoardCreator.getProfileImageUrl(), // 게시글 작성자 프로필 이미지 URL
                roomBoard.getRoom().getId(),
                roomBoard.getCreatedAt(),
                commentDtos
        );
    }
}

