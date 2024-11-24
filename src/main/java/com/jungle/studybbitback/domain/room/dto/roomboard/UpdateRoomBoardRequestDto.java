package com.jungle.studybbitback.domain.room.dto.roomboard;

import lombok.Getter;

@Getter
public class UpdateRoomBoardRequestDto {
    private String title;
    private String content;


    // 모든 필드를 초기화하는 생성자
    public UpdateRoomBoardRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
