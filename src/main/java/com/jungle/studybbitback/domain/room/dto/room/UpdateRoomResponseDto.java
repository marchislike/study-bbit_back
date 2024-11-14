package com.jungle.studybbitback.domain.room.dto.room;

import com.jungle.studybbitback.domain.room.entity.Room;
import lombok.Getter;

@Getter
public class UpdateRoomResponseDto {
    private String detail;
    private String password;
    private String profileImageUrl;

    public UpdateRoomResponseDto(Room room) {

        this.detail = room.getDetail();
        this.password = room.getPassword();
        this.profileImageUrl = room.getProfileImageUrl();}
}
