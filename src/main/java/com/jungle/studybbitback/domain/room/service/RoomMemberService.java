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

        // 사용자가 해당 방의 멤버인지 확인
        if (!roomMemberRepository.findByRoomIdAndMemberId(roomId, memberId).isPresent()) {
            throw new IllegalArgumentException("해당 스터디룸의 멤버만 조회할 수 있습니다.");
        }

        // Room 객체를 조회하여 leaderId 가져오기
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 방이 존재하지 않습니다."));
        Long leaderId = room.getLeaderId();

        // 해당 방의 모든 멤버 조회
        List<RoomMember> roomMembers = roomMemberRepository.findByRoomId(roomId);
        if (roomMembers.isEmpty()) {
            throw new IllegalArgumentException("해당 방에 멤버가 없습니다.");
        }

        // DTO로 변환하여 반환 (leaderId 포함)
        return roomMembers.stream()
                .map(roomMember -> new GetRoomMemberResponseDto(roomMember, leaderId))
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

    @Transactional
    public InviteRoomMemberResponseDto inviteRoomMember(InviteRoomMemberRequestDto requestDto) {
        String email = requestDto.getEmail().trim();

        // 방 정보 조회
        Room room = roomRepository.findById(requestDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));

        // 현재 로그인된 사용자 정보 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getMemberId();

        // 초대자 역할이 방장인지 확인
        if (!room.getLeaderId().equals(memberId)) {
            throw new IllegalStateException("방장만 초대할 수 있습니다.");
        }

        // 빈 자리 확인
        if (room.getParticipants() >= room.getMaxParticipants()) {
            throw new IllegalStateException("해당 방은 이미 가득 찼습니다.");
        }


        // 초대할 사용자가 존재하는지 확인
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 이미 방의 멤버인지 확인
        if (roomMemberRepository.existsByRoomAndMember(room, member)) {
            throw new IllegalStateException("이미 해당 방의 멤버입니다.");
        }

        // RoomMember 추가 및 participants 증가
        RoomMember newRoomMember = new RoomMember(room, member);
        roomMemberRepository.save(newRoomMember);
        room.increaseParticipants(); // Room Entity에 정의된 메서드

        // 응답 DTO 반환
        return new InviteRoomMemberResponseDto(newRoomMember);
    }


    //스터디룸 나가기
    @Transactional
    public String leaveRoom(Long roomId) {

        // 현재 로그인된 사용자 정보 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getMemberId();

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));

        // 리더는 방을 나갈 수 없도록 처리
        if (room.getLeaderId().equals(memberId)) {
            throw new IllegalStateException("방장은 방을 나갈 수 없습니다. 방을 삭제하거나 다른 멤버에게 방장 권한을 위임해야 합니다.");
        }

        RoomMember roomMember = roomMemberRepository.findByRoomIdAndMemberId(roomId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 스터디그룹에 속해있지 않습니다."));
        roomMemberRepository.delete(roomMember);
        return "스터디룸을 떠납니다.";
    }
}

