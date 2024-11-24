package com.jungle.studybbitback.domain.room.entity.roomboard;

import com.jungle.studybbitback.common.entity.ModifiedTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class RoomBoardComment extends ModifiedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_board_comment_id")
    private Long id;

    @Column(name = "content", nullable = false, length = 255)
    private String content;

    @Column(name = "created_by")
    private Long createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_board_id")
    private RoomBoard roomBoard;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onPrePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // 생성자
    @Builder
    public RoomBoardComment(String content, Long createdBy, RoomBoard roomBoard) {
        this.content = content;
        this.createdBy = createdBy;
        this.roomBoard = roomBoard;
    }

    // 댓글 수정
    public void updateComment(String content) {
        this.content = content;
    }

}
