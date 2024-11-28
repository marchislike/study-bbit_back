package com.jungle.studybbitback.domain.room.dto.roomboardcomment;

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
    private String createdByProfileUrl;
    private Long roomBoardId;

    //
    public GetRoomBoardCommentResponseDto(Long id, String content, String createdBy, String createdByProfileUrl, LocalDateTime createdAt, Long roomBoardId) {
        this.id = id;
        this.content = content;
        this.createdBy = createdBy;
        this.createdByProfileUrl = createdByProfileUrl; // 생성자에서 프로필 이미지 URL 초기화
        this.createdAt = createdAt;
        this.roomBoardId = roomBoardId;
    }
}
