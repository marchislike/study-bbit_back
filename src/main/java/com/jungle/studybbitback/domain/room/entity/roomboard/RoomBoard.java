package com.jungle.studybbitback.domain.room.entity.roomboard;

import com.jungle.studybbitback.common.entity.CreatedEntity;
import com.jungle.studybbitback.domain.room.dto.roomboard.UpdateRoomBoardRequestDto;
import com.jungle.studybbitback.domain.room.entity.Room;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RoomBoard extends CreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_board_id")
    private Long id;

    @Column(name = "room_board_title")
    private String title;

    @Column(name = "room_board_content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;


    public RoomBoard(Room room, String title, String content, Long createdBy) {
        this.room = room;
        this.title = title;
        this.content = content;
    }

//    public void updateContent(UpdateRoomBoardRequestDto requestDto) {
//        this.title = requestDto.getTitle();
//        this.content = requestDto.getContent();
//    }

}
