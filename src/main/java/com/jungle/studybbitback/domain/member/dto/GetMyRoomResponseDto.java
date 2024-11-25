package com.jungle.studybbitback.domain.member.dto;

import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.member.repository.MemberRepository;
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

    public GetMyRoomResponseDto(Page<Room> roomPage, MemberRepository memberRepository) {
        this.myRooms = roomPage.stream()
                .map(room -> {
                    // 방장 정보를 조회
                    Member leader = memberRepository.findById(room.getLeaderId())
                            .orElseThrow(() -> new IllegalArgumentException("방장 정보를 찾을 수 없습니다."));
                    // DTO 생성
                    return new GetRoomResponseDto(room, leader.getProfileImageUrl(), leader.getNickname());
                })
                .collect(Collectors.toList());
        this.totalPages = roomPage.getTotalPages();
        this.totalElements = roomPage.getTotalElements();
    }
}
