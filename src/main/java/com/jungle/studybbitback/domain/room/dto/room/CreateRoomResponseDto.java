package com.jungle.studybbitback.domain.room.dto.room;

import com.jungle.studybbitback.domain.room.entity.Room;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateRoomResponseDto {
    private String name;
    private String roomUrl;
    private String password;
    private String detail;
    private Integer maxParticipants;
    private String profileImageUrl;

    private LocalDateTime createdAt;

    public CreateRoomResponseDto(Room room) {
        this.name = room.getName();
        this.roomUrl = room.getRoomUrl();
        this.password = room.getPassword();
        this.detail = room.getDetail();
        this.maxParticipants = room.getMaxParticipants();
        this.profileImageUrl = room.getProfileImageUrl();
        this.createdAt = room.getCreatedAt();
    }
}
