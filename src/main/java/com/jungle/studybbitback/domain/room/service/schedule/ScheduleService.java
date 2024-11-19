package com.jungle.studybbitback.domain.room.service.schedule;

import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.member.repository.MemberRepository;
import com.jungle.studybbitback.domain.room.dto.schedule.CreateScheduleRequestDto;
import com.jungle.studybbitback.domain.room.dto.schedule.CreateScheduleResponseDto;
import com.jungle.studybbitback.domain.room.dto.schedule.GetScheduleDetailResponseDto;
import com.jungle.studybbitback.domain.room.dto.schedule.GetScheduleResponseDto;
import com.jungle.studybbitback.domain.room.entity.Room;
import com.jungle.studybbitback.domain.room.entity.schedule.Schedule;
import com.jungle.studybbitback.domain.room.entity.schedule.ScheduleMember;
import com.jungle.studybbitback.domain.room.respository.RoomMemberRepository;
import com.jungle.studybbitback.domain.room.respository.RoomRepository;
import com.jungle.studybbitback.domain.room.respository.schedule.ScheduleMemberRepository;
import com.jungle.studybbitback.domain.room.respository.schedule.ScheduleRepository;
import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMemberRepository scheduleMemberRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final RoomMemberRepository roomMemberRepository;

    @Transactional
    public CreateScheduleResponseDto createSchedule(CreateScheduleRequestDto requestDto) {

        // 현재 로그인된 사용자 정보 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getMemberId();

        // Member 엔터티 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // Room 엔터티 조회
        Room room = roomRepository.findById(requestDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("스터디룸이 존재하지 않습니다."));

        // 스터디룸 멤버 여부 확인
        if (roomMemberRepository.findByRoomIdAndMemberId(requestDto.getRoomId(), memberId).isEmpty()) {
            throw new AccessDeniedException("해당 스터디룸에 가입된 사용자만 일정을 생성할 수 있습니다.");
        }

        // 현재 연도 가져오기
        int currentYear = LocalDate.now().getYear();

        // 시작 날짜에 연도만 현재 연도로 변경 (기존 연도는 그대로 두고, 현재 연도로만 수정)
        LocalDateTime adjustedStartDateTime = requestDto.getStartDateTime().withYear(currentYear);
        LocalDateTime adjustedEndDateTime = requestDto.getEndDateTime().withYear(currentYear);

        // 종료 시간이 시작 시간보다 앞서지 않도록 검증
        if (adjustedEndDateTime.isBefore(adjustedStartDateTime)) {
            throw new IllegalArgumentException("종료 시간이 시작 시간보다 과거일 수 없습니다.");
        }

        // 일정 생성 및 저장
        Schedule schedule = Schedule.from(requestDto, room, member);
        schedule.setStartDateTime(adjustedStartDateTime);
        schedule.setEndDateTime(adjustedEndDateTime);

        Schedule savedSchedule = scheduleRepository.save(schedule);

        // DTO 변환 및 반환
        return CreateScheduleResponseDto.from(savedSchedule);
    }

    @Transactional(readOnly = true)
    public Page<GetScheduleResponseDto> getSchedulesByMonth(Long roomId, int year, int month, LocalDate startDate, LocalDate endDate, int size) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getMemberId();

        // 스터디룸 멤버 여부 확인
        if (roomMemberRepository.findByRoomIdAndMemberId(roomId, memberId).isEmpty()) {
            throw new AccessDeniedException("해당 스터디룸에 가입된 사용자만 접근할 수 있습니다.");
        }

        // 날짜 범위 계산
        if (startDate == null) {
            startDate = LocalDate.of(year, month, 1);  // 기본값: 해당 월의 첫 날
        }
        if (endDate == null) {
            endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());  // 기본값: 해당 월의 마지막 날
        }

        // 월의 첫 날과 마지막 날 계산
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        // 페이지네이션 처리
        Pageable pageable = PageRequest.of(0, size);  // 0 페이지에서 size 크기만큼 반환

        // 월별 일정 조회
        Page<Schedule> schedules = scheduleRepository.findByRoomIdAndStartDateTimeBetween(roomId, startOfMonth, endOfMonth, pageable);

        // DTO 변환 및 반환
        return schedules.map(GetScheduleResponseDto::from);
    }

    @Transactional(readOnly = true)
    public GetScheduleDetailResponseDto getScheduleDetail(Long scheduleId) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getMemberId();

        //일정 조회
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(()-> new IllegalArgumentException("해당 일정이 존재하지 않습니다."));

        // 해당 일정의 방 정보 확인 (roomId 확인) : scheduleRoomId
        Long scheduleRoomId = schedule.getRoom().getId();  // Schedule에서 Room을 통해 roomId 가져오기

        // 스터디룸 멤버 여부 확인
        if (roomMemberRepository.findByRoomIdAndMemberId(scheduleRoomId, memberId).isEmpty()) {
            throw new AccessDeniedException("해당 스터디룸에 가입된 사용자만 접근할 수 있습니다.");
        }

        //해당 일정의 참석 멤버 목록 조회
        List<ScheduleMember> scheduleMembers = scheduleMemberRepository.findByScheduleId(scheduleId);

        //참석 상태 목록 생성
        return GetScheduleDetailResponseDto.from(schedule, scheduleMembers);
    }

}


