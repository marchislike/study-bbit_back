package com.jungle.studybbitback.domain.room.dto.roomboardcomment;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetRoomBoardCommentResponseDto {
    private Long id;
    private String content;
    private Long memberId;
    private String createdByNickname;
    private String createdByProfileUrl;
    private LocalDateTime createdAt;
    private Long roomBoardId;

    //
    public GetRoomBoardCommentResponseDto(Long id, String content, Long memberId, String createdByNickname, String createdByProfileUrl, LocalDateTime createdAt, Long roomBoardId) {
        this.id = id;
        this.content = content;
        this.memberId = memberId;
        this.createdByNickname = createdByNickname;
        this.createdByProfileUrl = createdByProfileUrl; // 생성자에서 프로필 이미지 URL 초기화
        this.createdAt = createdAt;
        this.roomBoardId = roomBoardId;
    }
}
