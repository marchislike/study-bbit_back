package com.jungle.studybbitback.domain.room.dto.roomboard;

import lombok.Getter;

@Getter

public class CreateRoomBoardRequestDto {
    private Long roomId;
    private String title;
    private String content;
}
