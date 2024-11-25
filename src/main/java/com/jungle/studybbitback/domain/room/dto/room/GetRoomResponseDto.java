package com.jungle.studybbitback.domain.room.dto.room;

import com.jungle.studybbitback.domain.room.entity.Room;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetRoomResponseDto {
    private Long id;
    private String name;
    private String detail;
    private Integer participants;
    private Integer maxParticipants;
    private String profileImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private boolean isPrivate;
    private Long leaderId;
    private String leaderNickname;
    private String leaderImageUrl;

    public GetRoomResponseDto(Room room, String leaderNickname, String leaderImageUrl) {
        this.id = room.getId();
        this.name = room.getName();
        this.detail = room.getDetail();
        this.participants = room.getParticipants();
        this.maxParticipants = room.getMaxParticipants();
        this.profileImageUrl = room.getProfileImageUrl();
        this.createdAt = room.getCreatedAt();
        this.modifiedAt = room.getModifiedAt();
        this.isPrivate = room.isPrivate();
        this.leaderId = room.getLeaderId();
        this.leaderNickname = leaderNickname;
        this.leaderImageUrl = leaderImageUrl;
    }
}
