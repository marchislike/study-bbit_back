package com.jungle.studybbitback.domain.room.dto.roomboardcomment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateRoomBoardCommentRequestDto {
    private Long roomBoardId;
    private String content;
}
