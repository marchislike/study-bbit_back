package com.jungle.studybbitback.domain.room.dto.roomboard;

import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.room.entity.roomboard.RoomBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetRoomBoardResponseDto {
    private Long roomBoardId;
    private String title;
    private String content;
    private String createdBy;
    private String createdByProfileUrl;
    private LocalDateTime createdAt;
    private Long roomId;

    // RoomBoard 엔티티를 DTO로 변환
    public static GetRoomBoardResponseDto from(RoomBoard roomBoard, Member member) {
        return new GetRoomBoardResponseDto(
                roomBoard.getId(),
                roomBoard.getTitle(),
                roomBoard.getContent(),
                member.getNickname(), // 닉네임을 외부에서 전달
                member.getProfileImageUrl(),
                roomBoard.getCreatedAt(),
                roomBoard.getRoom().getId()
        );
    }
}
