package com.jungle.studybbitback.domain.member.service;

import com.jungle.studybbitback.common.file.service.FileService;
import com.jungle.studybbitback.domain.member.dto.GetMyRoomResponseDto;
import com.jungle.studybbitback.domain.member.dto.SignupRequestDto;
import com.jungle.studybbitback.domain.member.dto.UpdateMemberRequestDto;
import com.jungle.studybbitback.domain.member.dto.UpdateMemberResponseDto;
import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.member.entity.MemberRoleEnum;
import com.jungle.studybbitback.domain.member.repository.MemberRepository;
import com.jungle.studybbitback.domain.room.entity.Room;
import com.jungle.studybbitback.domain.room.entity.RoomMember;
import com.jungle.studybbitback.domain.room.respository.RoomMemberRepository;
import com.jungle.studybbitback.domain.room.respository.RoomRepository;
import com.jungle.studybbitback.jwt.JWTUtil;
import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {
    private final JWTUtil jwtUtil;

    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final FileService fileService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        String email = signupRequestDto.getEmail();
        String password = signupRequestDto.getPassword();
        String nickname = signupRequestDto.getNickname();

        log.info("email : {}", email);

        if(memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        Member member = new Member(email, bCryptPasswordEncoder.encode(password), nickname, MemberRoleEnum.ROLE_USER);

        memberRepository.save(member);
    }

	public boolean isExistNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
	}

    @Transactional
    public UpdateMemberResponseDto updateMember(Long memberId, UpdateMemberRequestDto requestDto) {

        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("가입되지 않은 회원입니다.")
        );

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long sessionMemberId = userDetails.getMemberId();
        if(memberId != sessionMemberId) {
            throw new IllegalArgumentException("잘못된 로그인 정보입니다.");
        }
        log.info("request Dto = {}", requestDto.getNickname());
        log.info("request Dto = {}", requestDto.getMemberProfile());

        String password = "";
        if(StringUtils.hasText(requestDto.getPassword())) {
            password = bCryptPasswordEncoder.encode(requestDto.getPassword());
        }

        String profileUrl = "";
        if(requestDto.isProfileChanged()) { // 프로필 변경된 경우
            if(StringUtils.hasText(member.getProfileImageUrl())) { // 이전에 저장된 것 있으면 삭제
                fileService.deleteFile(member.getProfileImageUrl());
            }
            if(!requestDto.getMemberProfile().isEmpty()) { // 새로 올린 것 있으면 저장
                profileUrl = fileService.uploadFile(requestDto.getMemberProfile(), "image", 0L);
                log.info("profileUrl = {}", profileUrl);
            }
        }

        member.updateMember(requestDto, password, profileUrl);

        return new UpdateMemberResponseDto(member);
    }

    @Transactional
    public GetMyRoomResponseDto getUserStudyRooms(Long memberId, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        // RoomRepository에서 수정된 메서드를 사용하여 Member가 속한 Room을 페이지로 조회
        Page<Room> roomPage = roomRepository.findByRoomMembersMember(member, pageable);

        return new GetMyRoomResponseDto(roomPage);
    }

}
