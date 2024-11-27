package com.jungle.studybbitback.domain.room.service;

import com.jungle.studybbitback.domain.dm.dto.GetDmResponseDto;
import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.member.repository.MemberRepository;
import com.jungle.studybbitback.domain.room.dto.banmember.BanRoomMemberRequestDto;
import com.jungle.studybbitback.domain.room.dto.banmember.BanRoomMemberResponseDto;
import com.jungle.studybbitback.domain.room.dto.banmember.GetBlacklistResponseDto;
import com.jungle.studybbitback.domain.room.dto.roommember.JoinRoomMemberRequestDto;
import com.jungle.studybbitback.domain.room.dto.roommember.JoinRoomMemberResponseDto;
import com.jungle.studybbitback.domain.room.dto.roommember.InviteRoomMemberRequestDto;
import com.jungle.studybbitback.domain.room.dto.roommember.InviteRoomMemberResponseDto;
import com.jungle.studybbitback.domain.room.dto.roommember.GetRoomMemberResponseDto;
import com.jungle.studybbitback.domain.room.entity.Room;
import com.jungle.studybbitback.domain.room.entity.RoomBlacklist;
import com.jungle.studybbitback.domain.room.entity.RoomMember;
import com.jungle.studybbitback.domain.room.respository.RoomBlacklistRepository;
import com.jungle.studybbitback.domain.room.respository.RoomMemberRepository;
import com.jungle.studybbitback.domain.room.respository.RoomRepository;
import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final RoomBlacklistRepository roomBlacklistRepository;

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

        // 로그인한 사용자 정보 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getMemberId();

        // 방 정보 조회
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 해당 방의 멤버라면 바로 입장 처리
        if (roomMemberRepository.existsByRoomAndMember(room, member)) {
            return new JoinRoomMemberResponseDto(roomId, memberId, room.getParticipants(), "스터디룸 '" + room.getName() + "'에 입장했습니다.");
        }

        // 빈 자리 확인
        if (room.getParticipants() >= room.getMaxParticipants()) {
            throw new IllegalStateException("해당 방은 이미 가득 찼습니다.");
        }

        // 비공개 방일 경우 비밀번호 검증
        if (room.isPrivate()) {
            if (requestDto.getPassword() == null || !room.getPassword().equals(requestDto.getPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        }

        // 신규 멤버 참여 처리: RoomMember 생성 및 participants 증가
        RoomMember roomMember = new RoomMember(room, member);
        roomMemberRepository.save(roomMember);
        room.increaseParticipants();

        int participantCount = roomMemberRepository.countByRoom(room);
        return new JoinRoomMemberResponseDto(roomId, memberId, participantCount, "귀여운 '" + member.getNickname() +"님이 방에 참여했습니다.");
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

        // 참여자 수 감소
        room.decreaseParticipants();
        return "스터디룸을 떠납니다.";
    }

    @Transactional
	public BanRoomMemberResponseDto banRoomMember(BanRoomMemberRequestDto request) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginMemberId = userDetails.getMemberId();

        Long banMemberId = request.getBanMemberId();
        Long roomId = request.getRoomId();

        RoomMember roomMember = roomMemberRepository.findByRoomIdAndMemberId(roomId, banMemberId)
                .orElseThrow(() -> new IllegalArgumentException("방 가입 회원이 아닙니다."));

        if(loginMemberId != roomMember.getRoom().getLeaderId()) {
            throw new IllegalStateException("방장만이 강퇴할 수 있습니다.");
        }

        roomMemberRepository.deleteByRoomIdAndMemberId(roomId, banMemberId);

        RoomBlacklist roomBlacklist = new RoomBlacklist(roomMember.getRoom(), roomMember.getMember(), request.getDetail());
        RoomBlacklist savedRoomBlacklist = roomBlacklistRepository.saveAndFlush(roomBlacklist);

        return new BanRoomMemberResponseDto(savedRoomBlacklist);
	}

    @Transactional
    public BanRoomMemberResponseDto unbanRoomMember(BanRoomMemberRequestDto request) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginMemberId = userDetails.getMemberId();

        Long roomId = request.getRoomId();
        Long banMemberId = request.getBanMemberId();

        RoomBlacklist roomBlacklist = roomBlacklistRepository.findByRoomIdAndMemberId(roomId, banMemberId)
                .orElseThrow(() -> new IllegalArgumentException("블랙리스트에 없습니다."));

        if(loginMemberId != roomBlacklist.getRoom().getLeaderId()) {
            throw new IllegalStateException("방장만이 강퇴해제할 수 있습니다.");
        }

        roomBlacklistRepository.deleteByRoomIdAndMemberId(roomId, banMemberId);

        return new BanRoomMemberResponseDto(roomBlacklist);
    }

    public Page<GetBlacklistResponseDto> getBlacklist(Long roomId, Pageable pageable) {
        
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginMemberId = userDetails.getMemberId();

        if(loginMemberId != room.getLeaderId()) {
            throw new IllegalStateException("방장만이 조회할 수 있습니다.");
        }

        return roomBlacklistRepository.findByRoomId(roomId, pageable).map(GetBlacklistResponseDto::new);
    }
}

