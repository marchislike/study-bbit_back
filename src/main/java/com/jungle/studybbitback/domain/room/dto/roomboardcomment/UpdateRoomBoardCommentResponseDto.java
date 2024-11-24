package com.jungle.studybbitback.domain.room.dto.roomboardcomment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UpdateRoomBoardCommentResponseDto {
    private Long id;
    private String content;
    private String createdByNickname;
    private LocalDateTime modifiedAt;
    private Long roomBoardId;

    @Builder
    public UpdateRoomBoardCommentResponseDto(Long id, String content, String createdByNickname, LocalDateTime modifiedAt, Long roomBoardId) {
        this.id = id;
        this.content = content;
        this.createdByNickname = createdByNickname;
        this.modifiedAt = modifiedAt;
        this.roomBoardId = getRoomBoardId();
    }
}
