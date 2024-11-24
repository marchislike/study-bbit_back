package com.jungle.studybbitback.domain.member.controller;

import com.jungle.studybbitback.domain.member.dto.GetMyRoomResponseDto;
import com.jungle.studybbitback.domain.member.dto.SignupRequestDto;
import com.jungle.studybbitback.domain.member.dto.UpdateMemberRequestDto;
import com.jungle.studybbitback.domain.member.dto.UpdateMemberResponseDto;
import com.jungle.studybbitback.domain.member.service.MemberService;
import com.jungle.studybbitback.domain.room.entity.Room;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    
    // 회원가입
    @PostMapping("/signup")
    public String signup(@Valid @RequestBody SignupRequestDto signupRequestDto, HttpServletResponse response) {
        memberService.signup(signupRequestDto);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return "success";
    }
    
    // 닉네임 중복 체크
    @GetMapping("/isExist/{nickname}")
    public boolean isExistNickname(@PathVariable("nickname") String nickname) {
        return memberService.isExistNickname(nickname);
    }
    
    // 회원수정
    @PostMapping(value = "/{memberId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateMemberResponseDto> updateMember(
            @PathVariable("memberId") Long memberId,
            @ModelAttribute UpdateMemberRequestDto requestDto) {
        UpdateMemberResponseDto responseDto = memberService.updateMember(memberId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 내 스터디 조회
    @GetMapping("/room/{memberId}")
    public ResponseEntity<GetMyRoomResponseDto> getUserStudyRooms(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size) {
        GetMyRoomResponseDto responseDto = memberService.getUserStudyRooms(memberId, page, size);
        return ResponseEntity.ok(responseDto);
    }
}
