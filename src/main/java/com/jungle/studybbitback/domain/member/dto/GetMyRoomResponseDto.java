package com.jungle.studybbitback.domain.member.dto;

import com.jungle.studybbitback.domain.room.dto.room.GetRoomResponseDto;
import com.jungle.studybbitback.domain.room.entity.Room;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GetMyRoomResponseDto {

    private List<GetRoomResponseDto> myRooms;
    private int totalPages;
    private long totalElements;

    public GetMyRoomResponseDto(Page<Room> roomPage) {
        this.myRooms = roomPage.stream()
                .map(GetRoomResponseDto::new)
                .collect(Collectors.toList());
        this.totalPages = roomPage.getTotalPages();
        this.totalElements = roomPage.getTotalElements();

    }
}
