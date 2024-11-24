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
    private String createdBy; // 게시글 작성자 (닉네임)
    private Long roomId;
    private LocalDateTime createdAt;
    private Page<GetRoomBoardCommentResponseDto> comments; // 게시글에 대한 댓글 리스트

    public static GetRoomBoardDetailResponseDto from(RoomBoard roomBoard, String createdByNickname, Page<RoomBoardComment> comments, MemberRepository memberRepository) {
        Page<GetRoomBoardCommentResponseDto> commentDtos = comments.map(comment -> {
            String commentCreatedByNickname = memberRepository.findById(comment.getCreatedBy())
                    .map(Member::getNickname)
                    .orElse("알 수 없음");
            return new GetRoomBoardCommentResponseDto(
                    comment.getId(),
                    comment.getContent(),
                    commentCreatedByNickname,
                    comment.getCreatedAt(),
                    roomBoard.getId()
            );
        });

        // 모든 필드를 초기화하는 생성자 사용
        return new GetRoomBoardDetailResponseDto(
                roomBoard.getId(),
                roomBoard.getTitle(),
                roomBoard.getContent(),
                createdByNickname,
                roomBoard.getRoom().getId(),
                roomBoard.getCreatedAt(),
                commentDtos
        );
    }
}
