package com.jungle.studybbitback.domain.room.dto.roomboardcomment;

import com.jungle.studybbitback.domain.room.entity.roomboard.RoomBoardComment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CreateRoomBoardCommentResponseDto {
    private Long id;
    private String content;
    private String createdByNickname;
    private LocalDateTime createdAt;
    private Long roomBoardId;

    public CreateRoomBoardCommentResponseDto(Long id, String content, String createdByNickname, LocalDateTime createdAt, Long roomBoardId) {
        this.id = id;
        this.content = content;
        this.createdByNickname = createdByNickname;
        this.createdAt = createdAt;
        this.roomBoardId = roomBoardId;
    }

    // 정적 팩토리 메서드
    public static CreateRoomBoardCommentResponseDto from(RoomBoardComment comment, String createdByNickname) {
        return new CreateRoomBoardCommentResponseDto(
                comment.getId(),
                comment.getContent(),
                createdByNickname,
                comment.getCreatedAt(),
                comment.getRoomBoard().getId()
        );

    }
}

