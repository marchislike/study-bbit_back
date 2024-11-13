package com.jungle.studybbitback.domain.room.service;

import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.member.repository.MemberRepository;
import com.jungle.studybbitback.domain.room.dto.roommember.InviteRoomMemberRequestDto;
import com.jungle.studybbitback.domain.room.dto.roommember.InviteRoomMemberResponseDto;
import com.jungle.studybbitback.domain.room.dto.roommember.LeaveRoomMemberRequestDto;
import com.jungle.studybbitback.domain.room.dto.roommember.GetRoomMemberResponseDto;
import com.jungle.studybbitback.domain.room.entity.Room;
import com.jungle.studybbitback.domain.room.entity.RoomMember;
import com.jungle.studybbitback.domain.room.respository.RoomMemberRepository;
import com.jungle.studybbitback.domain.room.respository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RoomMemberService {

    private final RoomMemberRepository roomMemberRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

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


    public GetRoomMemberResponseDto getRoomMember(Long roomId, Long memberId) {
        RoomMember roomMember = roomMemberRepository.findByRoomIdAndMemberId(roomId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 방에 존재하지 않는 회원입니다."));
        return new GetRoomMemberResponseDto(roomMember);
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

