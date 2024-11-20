package com.jungle.studybbitback.domain.room.dto.roomboard;

import com.jungle.studybbitback.domain.member.repository.MemberRepository;
import com.jungle.studybbitback.domain.room.entity.roomboard.RoomBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetRoomBoardDetailResponseDto {
    private Long roomBoardId;
    private String title;
    private String content;
    private String createdBy; // 게시글 작성자 (닉네임)
    private Long roomId;
    private LocalDateTime createdAt;

    // RoomBoard 엔티티를 DTO로 변환 (닉네임을 외부에서 입력받음)
    public static GetRoomBoardDetailResponseDto from(RoomBoard roomBoard, String createdByNickname) {
        return new GetRoomBoardDetailResponseDto(
                roomBoard.getId(),
                roomBoard.getTitle(),
                roomBoard.getContent(),
                createdByNickname, // 닉네임을 외부에서 전달
                roomBoard.getRoom().getId(),
                roomBoard.getCreatedAt()
        );
    }
}
