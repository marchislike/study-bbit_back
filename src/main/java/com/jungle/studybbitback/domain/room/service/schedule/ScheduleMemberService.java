package com.jungle.studybbitback.domain.room.service.schedule;

import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.member.repository.MemberRepository;
import com.jungle.studybbitback.domain.room.dto.schedulemember.*;
import com.jungle.studybbitback.domain.room.entity.Room;
import com.jungle.studybbitback.domain.room.entity.schedule.Schedule;
import com.jungle.studybbitback.domain.room.entity.schedule.ScheduleMember;
import com.jungle.studybbitback.domain.room.respository.RoomMemberRepository;
import com.jungle.studybbitback.domain.room.respository.schedule.ScheduleMemberRepository;
import com.jungle.studybbitback.domain.room.respository.schedule.ScheduleRepository;
import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    // 일정 참여의사 등록
    @Transactional
    public CreateScheduleMemberResponseDto participateInSchedule(CreateScheduleMemberRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findByIdOrThrow(requestDto.getScheduleId());
        Member member = getAuthenticatedMember();

        //스터디룸 멤버인지 확인
        Room room = schedule.getRoom();
        validateRoomMember(room, member);

        ScheduleMember scheduleMember = scheduleMemberRepository.findByScheduleIdAndMemberId(schedule.getId(), member.getId())
                .orElse(new ScheduleMember(schedule, member, requestDto.getIsParticipated()));

        scheduleMemberRepository.save(scheduleMember);

        log.info("참여의사 등록 완료: scheduleId={}, memberId={}, isParticipated={}",
                schedule.getId(), member.getId(), requestDto.getIsParticipated());

        return CreateScheduleMemberResponseDto.from(scheduleMember);
    }

    // 일정 참석 명단 조회
    @Transactional(readOnly = true)
    public List<GetScheduleMemberResponseDto> getScheduleMembers(Long scheduleId) {
        log.info("일정 참석 명단 조회 요청 - scheduleId: {}", scheduleId);

        List<ScheduleMember> members = scheduleMemberRepository.findByScheduleId(scheduleId);
        log.info("일정 참석 멤버 수: {}", members.size());

        return members.stream()
                .map(GetScheduleMemberResponseDto::from)
                .toList();
    }

    // 일정 참여의사 변경
    @Transactional
    public UpdateScheduleParticipationResponseDto updateParticipation(UpdateScheduleParticipationRequestDto requestDto) {

        Member authenticatedMember = getAuthenticatedMember();
        Long memberId = authenticatedMember.getId();

        ScheduleMember scheduleMember = scheduleMemberRepository.findByScheduleIdAndMemberId(
                requestDto.getScheduleId(), memberId
        ).orElseThrow(() -> new IllegalArgumentException("일정 멤버 정보가 존재하지 않습니다."));

        //참여 상태 변경
        scheduleMember.setIsParticipated(requestDto.getIsParticipated());

        // 수동 플러시
        entityManager.flush();

        // 변경된 scheduleMember를 다시 조회하여 최신 상태 확인
        ScheduleMember updatedScheduleMember = scheduleMemberRepository.findById(scheduleMember.getId())
                .orElseThrow(() -> new IllegalArgumentException("변경된 데이터 조회 실패"));

        // 로그 출력
        log.info("참여 상태 변경 완료: scheduleId={}, memberId={}, isParticipated={}",
                requestDto.getScheduleId(), memberId, updatedScheduleMember.getIsParticipated());

        return UpdateScheduleParticipationResponseDto.from(scheduleMember);
    }

    // 일정 참여의사 삭제
    @Transactional
    public void deleteParticipation(Long scheduleId) {

        // 로그인된 사용자 정보 가져오기
        Member authenticatedMember = getAuthenticatedMember();
        Long memberId = authenticatedMember.getId();

        ScheduleMember scheduleMember = scheduleMemberRepository.findByScheduleIdAndMemberId(scheduleId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("일정 멤버 정보가 존재하지 않습니다."));

        scheduleMemberRepository.delete(scheduleMember);
        log.info("참여의사 삭제 완료: scheduleId={}, memberId={}", scheduleId, memberId);
    }


    /* 편의용 코드 */

    private Member getAuthenticatedMember() { // 로그인한 사용자인지 확인
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getMemberId();

        // MemberRepository를 통해 Member 조회
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요합니다."));
    }

    private void validateRoomMember(Room room, Member member) { // 스터디룸 멤버인지 확인
        boolean isMember = roomMemberRepository.existsByRoomAndMember(room, member);
        if (!isMember) {
            throw new IllegalArgumentException("해당 스터디룸에 속한 멤버가 아닙니다.");
        }
    }
}

