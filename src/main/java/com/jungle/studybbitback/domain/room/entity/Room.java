package com.jungle.studybbitback.domain.room.entity;

import com.jungle.studybbitback.common.entity.ModifiedTimeEntity;
import com.jungle.studybbitback.domain.room.dto.room.CreateRoomRequestDto;
import com.jungle.studybbitback.domain.room.dto.room.UpdateRoomRequestDto;
import com.jungle.studybbitback.domain.room.entity.roomboard.RoomBoard;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String roomUrl;
    private String password;
    private String detail;

    @Column(nullable = false)
    private Integer participants;

    @Column(nullable = false)
    private Integer maxParticipants;

    private String profileImageUrl;

    @Column(nullable = false, name = "leader_id")
    private Long leaderId;

    @Column(name = "meeting_id", columnDefinition = "UUID", unique = true)
    private UUID meetingId;

    @OneToMany(mappedBy = "room")
    private Set<RoomBoard> roomBoard = new HashSet<>();

    public Room(CreateRoomRequestDto requestDto, Long memberId) {
        this.name = requestDto.getName();
        this.roomUrl = requestDto.getRoomUrl();
        this.isPrivate = requestDto.isPrivate();
        this.password = this.isPrivate ? requestDto.getPassword() : null;
        this.detail = requestDto.getDetail();
        this.participants = 1;
        this.maxParticipants = requestDto.getMaxParticipants();
        this.profileImageUrl = requestDto.getProfileImageUrl();
        this.leaderId = memberId;
    }

//    // 테스트 전용 id 생성
//    public Room(Long id, CreateRoomRequestDto requestDto, Long leaderId) {
//        this.id = id; // 테스트용 id
//        this.name = requestDto.getName();
//        this.roomUrl = requestDto.getRoomUrl();
//        this.password = this.isPrivate ? requestDto.getPassword() : null;
//        this.detail = requestDto.getDetail();
//        this.participants = 1;
//        this.maxParticipants = requestDto.getMaxParticipants();
//        this.profileImageUrl = requestDto.getProfileImageUrl();
//        this.leaderId = leaderId;
//    }

    public void updateDetails(UpdateRoomRequestDto requestDto) {
        this.detail = requestDto.getDetail();
        this.profileImageUrl = requestDto.getProfileImageUrl();
        // 비공개 방에서만 비밀번호 수정
        if (this.isPrivate) {
            this.password = requestDto.getPassword();
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

    public void startMeeting() {
        this.meetingId = UUID.randomUUID(); //화상 회의 시작할 경우 화상회의id 생성
    }

    public void endMeeting() {
        this.meetingId = null;
    }
    //이하 구현 예정
}