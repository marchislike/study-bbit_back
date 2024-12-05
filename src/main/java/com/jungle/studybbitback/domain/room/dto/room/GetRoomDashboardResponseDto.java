package com.jungle.studybbitback.domain.room.dto.room;

import com.jungle.studybbitback.domain.room.dto.roomboard.GetRoomBoardDetailResponseDto;
import com.jungle.studybbitback.domain.room.entity.Room;
import lombok.Getter;

@Getter
public class GetRoomDashboardResponseDto {
    private Long id;
    private String name;
    private String detail;
    private Integer participants;
    private Integer maxParticipants;
    private Long leaderId;
    private GetRoomBoardDetailResponseDto notice; // 기존 상세 dto 그대로 활용



    public GetRoomDashboardResponseDto(Room room, GetRoomBoardDetailResponseDto notice) {
        this.id = room.getId();
        this.name = room.getName();
        this.detail = room.getDetail();
        this.participants = room.getParticipants();
        this.maxParticipants = room.getMaxParticipants();
        this.leaderId = room.getLeaderId();
        this.notice = notice;
    }
}
