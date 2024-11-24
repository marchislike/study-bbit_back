package com.jungle.studybbitback.domain.room.entity.roomboard;

import com.jungle.studybbitback.common.entity.CreatedEntity;
import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.room.dto.roomboard.UpdateRoomBoardRequestDto;
import com.jungle.studybbitback.domain.room.entity.Room;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Room room;

    @Column(name = "created_by")  // createdBy를 Member와 연결
    private Long createdBy;  // 작성자는 Member 객체로 연결

    @OneToMany(mappedBy = "roomBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomBoardComment> comments = new ArrayList<>();

    public RoomBoard(Room room, String title, String content, Long createdBy) {
        this.room = room;
        this.title = title;
        this.content = content;
        this.createdBy = createdBy;
    }

    // 작성자 닉네임 조회
    public String getCreatedByNickname(com.jungle.studybbitback.domain.member.repository.MemberRepository memberRepository) {
        return memberRepository.findById(this.createdBy)
                .map(member -> member.getNickname())
                .orElse("알 수 없음"); // 작성자를 찾을 수 없을 경우 기본값 반환
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
