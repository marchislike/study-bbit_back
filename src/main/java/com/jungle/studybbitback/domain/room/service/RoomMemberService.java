package com.jungle.studybbitback.domain.room.service;

import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.member.repository.MemberRepository;
import com.jungle.studybbitback.domain.room.dto.roommember.JoinRoomMemberRequestDto;
import com.jungle.studybbitback.domain.room.dto.roommember.JoinRoomMemberResponseDto;
import com.jungle.studybbitback.domain.room.dto.roommember.InviteRoomMemberRequestDto;
import com.jungle.studybbitback.domain.room.dto.roommember.InviteRoomMemberResponseDto;
import com.jungle.studybbitback.domain.room.dto.roommember.LeaveRoomMemberRequestDto;
import com.jungle.studybbitback.domain.room.dto.roommember.GetRoomMemberResponseDto;
import com.jungle.studybbitback.domain.room.entity.Room;
import com.jungle.studybbitback.domain.room.entity.RoomMember;
import com.jungle.studybbitback.domain.room.respository.RoomMemberRepository;
import com.jungle.studybbitback.domain.room.respository.RoomRepository;
import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RoomMemberService {

    private final RoomMemberRepository roomMemberRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    //스터디룸 전체 멤버 조회
    public List<GetRoomMemberResponseDto> getRoomMembers(Long roomId) {
        // 현재 로그인된 사용자 정보 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getMemberId();

        // 현재 사용자가 해당 스터디룸의 멤버인지 확인
        boolean isMember = roomMemberRepository.findByRoomIdAndMemberId(roomId, memberId).isPresent();
        if (!isMember) {
            throw new IllegalArgumentException("해당 스터디룸의 멤버만 조회할 수 있습니다.");
        }


        List<RoomMember> roomMember = roomMemberRepository.findByRoomId(roomId);
        if (roomMember.isEmpty()) {
            throw new IllegalArgumentException("해당 방에 멤버가 없습니다.");
        }
        return roomMember.stream()
                .map(GetRoomMemberResponseDto::new)
                .collect(Collectors.toList());
    }

    //스터디룸 참여(가입)
    @Transactional
    public JoinRoomMemberResponseDto joinRoom(Long roomId, JoinRoomMemberRequestDto requestDto) {
//        Long roomId = requestDto.getRoomId();

        // 로그인한 사용자 정보 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getMemberId();

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 이미 방에 참여한 경우 확인
        if (roomMemberRepository.existsByRoomAndMember(room, member)) {
            throw new IllegalStateException("이미 참여한 방입니다.");
        }

        // 방 참여 처리
        RoomMember roomMember = new RoomMember(room, member);
        roomMemberRepository.save(roomMember);

        int participantCount = roomMemberRepository.countByRoom(room);
        return new JoinRoomMemberResponseDto(roomId, memberId, participantCount, memberId +"님이 방에 참여했습니다.");
    }

    //스터디룸 초대
    @Transactional
    public InviteRoomMemberResponseDto inviteRoomMember(InviteRoomMemberRequestDto requestDto) {
        Long roomId = requestDto.getRoomId();
        String email = requestDto.getEmail();

        // 초대하려는 사용자의 정보를 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Room room = roomRepository.findById(requestDto.getRoomId()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 방입니다."));

        // 이미 초대된 회원인지 확인
        if (roomMemberRepository.existsByRoomAndMember(room, member)) {
            throw new IllegalStateException("이미 초대된 회원입니다.");
        }

        RoomMember roomMember = new RoomMember(room, member);
        RoomMember savedRoomMember = roomMemberRepository.save(roomMember);

        return new InviteRoomMemberResponseDto(savedRoomMember);
    }


    //스터디룸 나가기
    @Transactional
    public String leaveRoom(LeaveRoomMemberRequestDto requestDto) {
        RoomMember roomMember = roomMemberRepository.findByRoomIdAndMemberId(requestDto.getRoomId(), requestDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 스터디그룹에 속해있지 않습니다."));

        roomMemberRepository.delete(roomMember);
        return "스터디룸을 떠납니다.";
    }
}

