package com.jungle.studybbitback.domain.room.entity;

import com.jungle.studybbitback.common.entity.ModifiedTimeEntity;

import com.jungle.studybbitback.domain.room.dto.room.CreateRoomRequestDto;
import com.jungle.studybbitback.domain.room.dto.room.UpdateRoomRequestDto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Room extends ModifiedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @Column(name = "is_private", nullable = false)
    private boolean isPrivate = false; // default : 공개방

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;
    private String detail;

    @Column(nullable = false)
    private Integer participants;

    @Column(nullable = false)
    private Integer maxParticipants;

    private String profileImageUrl;

    @Column(nullable = false, name = "leader_id")
    private Long leaderId;

    // Room과 연결된 RoomMember를 통해 참여한 Member들을 조회
    @OneToMany(mappedBy = "room")
    private Set<RoomMember> roomMembers = new HashSet<>();

    public Room(CreateRoomRequestDto requestDto, Long memberId, String roomImageUrl) {
        this.name = requestDto.getName();
        this.isPrivate = requestDto.isPrivate();
        this.password = this.isPrivate ? requestDto.getPassword() : null;
        this.detail = requestDto.getDetail();
        this.participants = 1;
        this.maxParticipants = requestDto.getMaxParticipants();
        this.profileImageUrl = roomImageUrl;
        this.leaderId = memberId;
    }

    public void updateDetails(String detail, String password, String roomImageUrl, boolean isRoomImageChanged) {
        if (StringUtils.hasText(detail)) {
            this.detail = detail;
        }
        if (isRoomImageChanged) {
            this.profileImageUrl = roomImageUrl;
        }
        // 비공개 방에서만 비밀번호 수정
        if (this.isPrivate && StringUtils.hasText(password)) {
            this.password = password;
        }
    }

    // participants 증감 메서드 추가
    public void increaseParticipants() {
        if (this.participants >= this.maxParticipants) {
            throw new IllegalStateException("참여 가능 인원이 초과되었습니다.");
        }
        this.participants += 1;
    }

    public void decreaseParticipants() {
        if (this.participants > 0) {
            this.participants -= 1;
        }
    }
}