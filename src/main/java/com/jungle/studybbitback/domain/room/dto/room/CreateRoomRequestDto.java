package com.jungle.studybbitback.domain.room.dto.room;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class CreateRoomRequestDto {
    private String name;
    private String password;
    private String detail;
    private Integer participants;
    private Integer maxParticipants;
    private MultipartFile roomImage;
    private boolean isPrivate;


    @Builder
    public CreateRoomRequestDto(String name, String password, String detail, Integer participants, Integer maxParticipants, MultipartFile roomImage, Boolean isPrivate) {
        this.name = name;
        this.password = isPrivate ? password : null; //비공개 방일 경우만 비밀번호 설정
        this.detail = detail;
        this.participants = participants;
        this.maxParticipants = maxParticipants;
        this.roomImage = roomImage;
        this.isPrivate = isPrivate;

        // 비밀번호 확인을 위한 로그 추가
        System.out.println("비밀번호: " + this.password); // 디버깅용 로그
    }
}
