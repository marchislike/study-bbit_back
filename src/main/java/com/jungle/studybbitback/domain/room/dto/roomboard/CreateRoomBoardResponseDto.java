package com.jungle.studybbitback.domain.room.dto.roomboard;

import com.jungle.studybbitback.domain.room.entity.roomboard.RoomBoard;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateRoomBoardResponseDto {
    private Long roomBoardId;
    private String title;
    private String content;
    private String createdBy;
    private final LocalDateTime createdAt;

    public CreateRoomBoardResponseDto(Long roomBoardId, String title, String content, String createdBy, LocalDateTime createdAt) {
        this.roomBoardId = roomBoardId;
        this.title = title;
        this.content = content;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public static CreateRoomBoardResponseDto from(RoomBoard roomBoard, String createdByNickname) {
        return new CreateRoomBoardResponseDto(
                roomBoard.getId(),
                roomBoard.getTitle(),
                roomBoard.getContent(),
                createdByNickname,
                roomBoard.getCreatedAt()
        );
    }
}
