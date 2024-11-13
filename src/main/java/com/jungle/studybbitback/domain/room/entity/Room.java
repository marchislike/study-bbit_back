package com.jungle.studybbitback.domain.room.entity;

import com.jungle.studybbitback.common.entity.ModifiedTimeEntity;
import com.jungle.studybbitback.domain.room.dto.room.CreateRoomRequestDto;
import com.jungle.studybbitback.domain.room.dto.room.UpdateRoomRequestDto;
import com.jungle.studybbitback.domain.room.entity.roomboard.RoomBoard;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Room extends ModifiedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    //name, roomUrl, participants,max~는 변경 불가
    private String name;
    private String roomUrl;
    private String password;
    private String detail;
    private Integer participants;
    private Integer maxParticipants;
    private String profileImageUrl;

    @Column(name = "leader_id")
    private Long leaderId;

    @OneToMany (mappedBy = "room")
    private Set<RoomBoard> roomBoard = new HashSet<>();

    public Room(CreateRoomRequestDto requestDto, Long leaderId) {
        this.name = name;
        this.roomUrl = roomUrl;
        this.password = password;
        this.detail = detail;
        this.participants = participants;
        this.maxParticipants = maxParticipants;
        this.profileImageUrl = profileImageUrl;
        this.leaderId = leaderId;
    }

    public void updateDetails(UpdateRoomRequestDto requestDto) {
        this.password = requestDto.getPassword();
        this.detail = requestDto.getDetail();
        this.profileImageUrl = requestDto.getProfileImageUrl();
    }

}
