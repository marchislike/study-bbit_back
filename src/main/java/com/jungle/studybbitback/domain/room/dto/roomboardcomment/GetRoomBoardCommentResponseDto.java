package com.jungle.studybbitback.domain.room.dto.roomboardcomment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetRoomBoardCommentResponseDto {
    private Long id;
    private String content;
    private String createdBy;
    private LocalDateTime createdAt;
    private Long roomBoardId;

    // 명시적으로 생성자 정의
    public GetRoomBoardCommentResponseDto(Long id, String content, String createdBy, LocalDateTime createdAt, Long roomBoardId) {
        this.id = id;
        this.content = content;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.roomBoardId = roomBoardId;
    }
}
