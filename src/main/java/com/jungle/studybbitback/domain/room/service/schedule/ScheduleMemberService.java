package com.jungle.studybbitback.domain.room.service.schedule;

import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.member.repository.MemberRepository;
import com.jungle.studybbitback.domain.room.dto.schedulemember.*;
import com.jungle.studybbitback.domain.room.entity.Room;
import com.jungle.studybbitback.domain.room.entity.schedule.ParticipateStatusEnum;
import com.jungle.studybbitback.domain.room.entity.schedule.Schedule;
import com.jungle.studybbitback.domain.room.entity.schedule.ScheduleMember;
import com.jungle.studybbitback.domain.room.respository.RoomMemberRepository;
import com.jungle.studybbitback.domain.room.respository.schedule.ScheduleMemberRepository;
import com.jungle.studybbitback.domain.room.respository.schedule.ScheduleRepository;
import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ScheduleMemberService {
    private final EntityManager entityManager;

    private final ScheduleMemberRepository scheduleMemberRepository;
    private final ScheduleRepository scheduleRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final MemberRepository memberRepository;

    // 결석 등록
    @Transactional
    public ApplyNotedScheduleMemberResponseDto applyPreAbsenceScheduleMember(ApplyNotedScheduleMemberRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(requestDto.getScheduleId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 일정입니다."));
        Member member = getAuthenticatedMember();

        ScheduleMember scheduleMember = new ScheduleMember(schedule, member, ParticipateStatusEnum.NOTED, requestDto.getPreAbsenceDetail());

//        ScheduleMember scheduleMember = scheduleMemberRepository.findByScheduleIdAndMemberId(schedule.getId(), member.getId())
//                .orElse(new ScheduleMember(schedule, member, requestDto.getIsParticipated()));

        scheduleMemberRepository.save(scheduleMember);

        return ApplyNotedScheduleMemberResponseDto.from(scheduleMember);
    }
    
    // 결석 등록 제거
    @Transactional
    public String cancelPreAbsenceScheduleMember(Long scheduleId) {

        // 로그인된 사용자 정보 가져오기
        Long memberId = getAuthenticatedMemberId();

        ScheduleMember scheduleMember = scheduleMemberRepository.findByScheduleIdAndMemberId(scheduleId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("결석 신청 정보가 없습니다."));

        if (scheduleMember.getParticipateStatus() != ParticipateStatusEnum.NOTED) {
            throw new IllegalArgumentException("결석인 경우에만 취소 가능합니다.");
        }

        scheduleMemberRepository.delete(scheduleMember);
        return "Success";
    }
    
    // 출석부 등록
    @Transactional
    public List<ApplyScheduleMembersResponseDto> applyScheduleMembers(ApplyScheduleMembersRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(requestDto.getScheduleId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 일정입니다."));

        List<ScheduleMember> scheduleMembers = requestDto.getMembers().stream()
                .map(memberStatus -> {
                    Member member = memberRepository.findById(memberStatus.getMemberId())
                            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
                    ParticipateStatusEnum status = ParticipateStatusEnum.valueOf(memberStatus.getStatus());
                    // 매너온도 감소 로직 구현필요
                    return new ScheduleMember(schedule, member, status);
                })
                .collect(Collectors.toList());

        scheduleMemberRepository.saveAll(scheduleMembers);

        return scheduleMembers.stream()
                .map(ApplyScheduleMembersResponseDto::from)
                .collect(Collectors.toList());
    }

    // 일정 참석 명단 조회
    public Page<GetScheduleMemberResponseDto> getScheduleMembers(Long scheduleId, Pageable pageable) {
        return scheduleMemberRepository.findByScheduleId(scheduleId, pageable).map(GetScheduleMemberResponseDto::from);
    }

    /* 편의용 코드 */

    private Long getAuthenticatedMemberId() { // 로그인한 사용자 id 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getMemberId();
    }

    private Member getAuthenticatedMember() { // 로그인한 사용자 정보 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getMemberId();

        // MemberRepository를 통해 Member 조회
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
    }

    private void validateRoomMember(Room room, Member member) throws AccessDeniedException { // 스터디룸 멤버인지 확인
        boolean isMember = roomMemberRepository.existsByRoomAndMember(room, member);
        if (!isMember) {
            throw new AccessDeniedException("해당 스터디룸에 속한 멤버가 아닙니다.");
        }
    }
}

