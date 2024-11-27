package com.jungle.studybbitback.domain.room.service.schedule;

import com.jungle.studybbitback.common.utils.DateUtils;
import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.member.repository.MemberRepository;
import com.jungle.studybbitback.domain.room.controller.schedule.ScheduleCycleIdGenerator;
import com.jungle.studybbitback.domain.room.dto.schedule.CreateScheduleRequestDto;
import com.jungle.studybbitback.domain.room.dto.schedule.CreateScheduleResponseDto;
import com.jungle.studybbitback.domain.room.dto.schedule.GetScheduleDetailResponseDto;
import com.jungle.studybbitback.domain.room.dto.schedule.GetScheduleResponseDto;
import com.jungle.studybbitback.domain.room.entity.Room;
import com.jungle.studybbitback.domain.room.entity.schedule.Schedule;
import com.jungle.studybbitback.domain.room.respository.RoomMemberRepository;
import com.jungle.studybbitback.domain.room.respository.RoomRepository;
import com.jungle.studybbitback.domain.room.respository.schedule.ScheduleRepository;
import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final ScheduleCycleIdGenerator scheduleCycleIdGenerator;

    private void validateRoomMembership(Long roomId, Long memberId) {
        if (roomMemberRepository.findByRoomIdAndMemberId(roomId, memberId).isEmpty()) {
            throw new AccessDeniedException("해당 스터디룸에 가입된 사용자만 접근할 수 있습니다.");
        }
    }

    // 일정 생성
    @Transactional
    public CreateScheduleResponseDto createSchedule(CreateScheduleRequestDto requestDto) {
        // 인증된 사용자 정보 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getMemberId();

        // 사용자 및 스터디룸 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Room room = roomRepository.findById(requestDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("스터디룸이 존재하지 않습니다."));

        // 스터디룸 멤버 여부 확인
        validateRoomMembership(requestDto.getRoomId(), memberId);

        // 입력값 검증
        if (requestDto.getStartTime() == null || requestDto.getEndTime() == null) {
            throw new IllegalArgumentException("시작 시간과 종료 시간은 필수 입력값입니다.");
        }

        // 시작날짜에 대한 시작 및 종료 시간 병합
        LocalDateTime startDateTime = DateUtils.mergeDateAndTime(requestDto.getStartDate(), requestDto.getStartTime());
        LocalDateTime endDateTime = DateUtils.mergeDateAndTime(requestDto.getStartDate(), requestDto.getEndTime());
        log.info("시작시간: {}, 종료시간: {}", startDateTime, endDateTime);

        // 종료 시간이 시작 시간보다 앞서지 않도록 검증
        if (endDateTime.isBefore(startDateTime)) {
            throw new IllegalArgumentException("종료 시간이 시작 시간보다 과거일 수 없습니다.");
        }

        // 시작 및 종료 시간은 같은 날짜 내에서 설정 가능
        DateUtils.validateSameDate(startDateTime, endDateTime);

        // 주간 반복일정일 경우 종료날짜를 설정 필수
        if (requestDto.isRepeatFlag() && requestDto.getRepeatEndDate() == null) {
            throw new IllegalArgumentException("주간 반복 스터디는 종료 날짜를 반드시 입력해야 합니다.");
        }

        Long scheduleCycleId = null;

        // 반복 플래그가 true인 경우 scheduleCycleId 설정
        if (requestDto.isRepeatFlag()) {
            scheduleCycleId = scheduleCycleIdGenerator.generate();
            log.info("부여된 반복태그 scheduleCycleId: {}", scheduleCycleId);
        }

        Schedule savedSchedule = null;

        // 반복 플래그가 true인 경우 시작 날짜가 반복 요일에 포함되는지 확인
        boolean startDateInRepeatDays = true;
        if (requestDto.isRepeatFlag()) {
            List<DayOfWeek> repeatDays = DateUtils.parseDaysOfWeek(requestDto.getDaysOfWeek());
            startDateInRepeatDays = repeatDays.contains(requestDto.getStartDate().getDayOfWeek());
        }

        // 반복 일정이 아니거나, 시작 날짜가 반복 요일에 포함되는 경우에만 초기 일정 생성
        if (!requestDto.isRepeatFlag() || startDateInRepeatDays) {
            // 일정 생성
            Schedule schedule = Schedule.from(requestDto, room, member);
            schedule.setStartDateTime(startDateTime);
            schedule.setEndDateTime(endDateTime);
            schedule.setScheduleCycleId(scheduleCycleId);
            log.info("스터디 일정이 등록되었습니다: {}", schedule);

            // 일정 저장
            savedSchedule = scheduleRepository.save(schedule);
        }

        // 반복 일정 처리
        if (requestDto.isRepeatFlag()) {
            log.info("반복 일정 생성 시작");
            Schedule firstRecurringSchedule = createRecurringSchedules(savedSchedule, requestDto, startDateTime, endDateTime, room, member, scheduleCycleId);
            if (savedSchedule == null && firstRecurringSchedule != null) {
                savedSchedule = firstRecurringSchedule;
            }
        }

        // DTO 변환 및 반환
        if (savedSchedule != null) {
            return CreateScheduleResponseDto.from(savedSchedule);
        } else {
            // 초기 일정이 없고 반복 일정도 없을 경우 (비정상적인 상황)
            throw new IllegalStateException("일정 생성에 실패하였습니다.");
        }
    }
    // isRepeatFlag = True이면 주간반복 일정 생성

    private Schedule createRecurringSchedules(Schedule initialSchedule, CreateScheduleRequestDto requestDto,
                                              LocalDateTime startDateTime, LocalDateTime endDateTime,
                                              Room room, Member member, Long scheduleCycleId) {
        LocalDate startDate = startDateTime.toLocalDate(); // 시작 날짜
        LocalDate repeatEndDate = requestDto.getRepeatEndDate(); // 반복 종료 날짜
        List<DayOfWeek> repeatDays = DateUtils.parseDaysOfWeek(requestDto.getDaysOfWeek()); // 반복할 요일 리스트
        log.info("반복 요일: {}, 종료 날짜: {}", repeatDays, repeatEndDate);

        // 시작 날짜부터 반복 종료 날짜까지 주 단위로 반복
        LocalDate currentWeekStartDate = startDate; // 금주의 시작 날짜

        Schedule firstRecurringSchedule = null;

        while (!currentWeekStartDate.isAfter(repeatEndDate)) {
            // 현재 주의 모든 반복 요일에 대해 처리
            for (DayOfWeek dayOfWeek : repeatDays) {
                LocalDate currentDate = currentWeekStartDate.with(dayOfWeek);

                // 현재 날짜가 시작 날짜 이전이면 스킵
                if (currentDate.isBefore(startDate)) {
                    continue;
                }
                // 현재 날짜가 반복 종료 날짜 이후이면 스킵
                if (currentDate.isAfter(repeatEndDate)) {
                    continue;
                }
                // 이미 생성된 초기 일정과 날짜가 겹치면 스킵
                if (initialSchedule != null && currentDate.equals(startDate)) {
                    continue;
                }

                // 반복 일정의 시작/종료 시간 설정
                LocalDateTime currentStartDateTime = currentDate.atTime(startDateTime.toLocalTime());
                LocalDateTime currentEndDateTime = currentDate.atTime(endDateTime.toLocalTime());
                log.info("반복 일정 생성 - 날짜: {}, 시작 시간: {}, 종료 시간: {}", currentDate, currentStartDateTime, currentEndDateTime);

                // 반복 일정 엔터티 생성
                Schedule recurringSchedule = Schedule.from(
                        CreateScheduleRequestDto.builder()
                                .title(requestDto.getTitle())
                                .startDate(currentDate)
                                .startTime(currentStartDateTime.toLocalTime())
                                .endTime(currentEndDateTime.toLocalTime())
                                .detail(requestDto.getDetail())
                                .roomId(requestDto.getRoomId())
                                .repeatFlag(true)
                                .repeatPattern(requestDto.getRepeatPattern())
                                .daysOfWeek(requestDto.getDaysOfWeek())
                                .repeatEndDate(requestDto.getRepeatEndDate())
                                .build(),
                        room,
                        member
                );

                recurringSchedule.setStartDateTime(currentStartDateTime);
                recurringSchedule.setEndDateTime(currentEndDateTime);
                recurringSchedule.setScheduleCycleId(scheduleCycleId); // 동일한 scheduleCycleId 할당

                // 반복 일정 저장
                recurringSchedule = scheduleRepository.save(recurringSchedule);
                log.info("저장된 반복 일정: {}", recurringSchedule);

                // 첫 번째 반복 일정 저장
                if (firstRecurringSchedule == null) {
                    firstRecurringSchedule = recurringSchedule;
                }
            }
            // 다음 주로 이동
            currentWeekStartDate = currentWeekStartDate.plusWeeks(1);
        }

        return firstRecurringSchedule;
    }

    @Transactional
    public Page<GetScheduleResponseDto> getAllSchedules(Long roomId, Pageable pageable) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getMemberId();
        validateRoomMembership(roomId, memberId);


        Page<Schedule> schedules = scheduleRepository.findByRoomId(roomId, pageable);

        return schedules.map(GetScheduleResponseDto::from);
    }


    @Transactional(readOnly = true)
    public GetScheduleDetailResponseDto getScheduleDetail(Long scheduleId, Pageable commentPageable) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getMemberId();

        // 일정 조회
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 스터디가 존재하지 않습니다."));

        // 방 멤버 여부 검증
        Long roomId = schedule.getRoom().getId();
        validateRoomMembership(roomId, memberId);

        // 댓글 목록 조회 (댓글 기능 구현 시 주석 해제)
        // Page<Comment> comments = commentRepository.findByScheduleId(scheduleId, commentPageable);
        // List<CommentDto> commentDtos = comments.map(CommentDto::from).getContent();

        // TODO: 출석부 로직 추가

        return GetScheduleDetailResponseDto.builder()
                .scheduleId(schedule.getId())
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .startTime(schedule.getStartDateTime().toLocalTime())
                .endTime(schedule.getEndDateTime().toLocalTime())
                .detail(schedule.getDetail())
                .roomId(schedule.getRoom().getId())
                .creatorName(schedule.getCreatedBy().getNickname())
                .repeatFlag(schedule.isRepeatFlag())
                .repeatPattern(schedule.getRepeatPattern())
                .daysOfWeek(schedule.getDaysOfWeek())
                .repeatEndDate(schedule.getRepeatEndDate() != null ? schedule.getRepeatEndDate().toString() : null)
                .scheduleCycleId(schedule.getScheduleCycleId())
                .build();
    }



}
