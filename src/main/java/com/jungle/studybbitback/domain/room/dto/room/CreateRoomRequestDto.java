package com.jungle.studybbitback.domain.room.dto.room;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateRoomRequestDto {
    private String name;
    private String roomUrl;
    private String password;
    private String detail;
    private Integer participants;
    private Integer maxParticipants;
    private String profileImageUrl;

    @Builder
    public CreateRoomRequestDto(String name, String roomUrl, String password, String detail, Integer participants, Integer maxParticipants, String profileImageUrl) {
        this.name = name;
        this.roomUrl = roomUrl;
        this.password = password;
        this.detail = detail;
        this.participants = participants;
        this.maxParticipants = maxParticipants;
        this.profileImageUrl = profileImageUrl;
    }
}
