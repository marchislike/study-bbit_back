package com.jungle.studybbitback.domain.room.dto.roomboard;

import lombok.Getter;

@Getter
public class UpdateRoomBoardResponseDto {
    private Long id;
    private String title;
    private String content;

    public UpdateRoomBoardResponseDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
