package com.jungle.studybbitback.domain.room.service.schedule;

import com.jungle.studybbitback.common.utils.DateUtils;
import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.member.repository.MemberRepository;
import com.jungle.studybbitback.domain.room.controller.schedule.ScheduleCycleIdGenerator;
import com.jungle.studybbitback.domain.room.dto.schedule.*;
import com.jungle.studybbitback.domain.room.dto.schedulecomment.GetScheduleCommentResponseDto;
import com.jungle.studybbitback.domain.room.entity.Room;
import com.jungle.studybbitback.domain.room.entity.schedule.Schedule;
import com.jungle.studybbitback.domain.room.respository.RoomMemberRepository;
import com.jungle.studybbitback.domain.room.respository.RoomRepository;
import com.jungle.studybbitback.domain.room.respository.schedule.ScheduleCommentRepository;
import com.jungle.studybbitback.domain.room.respository.schedule.ScheduleRepository;
import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private final ScheduleCommentRepository scheduleCommentRepository;

    private void validateRoomMembership(Long roomId, Long memberId) {
        if (roomMemberRepository.findByRoomIdAndMemberId(roomId, memberId).isEmpty()) {
            throw new AccessDeniedException("해당 스터디룸에 가입된 사용자만 접근할 수 있습니다.");
        }
    }

    // scheduleCycleId를 새로 생성
    private Long generateNewScheduleCycleId() {
        return scheduleRepository.findMaxScheduleCycleId().orElse(0L) + 1;
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
                                .day(requestDto.getDay())
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

    // 일정 댓글용 조회 메서드
    @Transactional(readOnly = true)
    public Schedule getSchedule(Long scheduleId) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getMemberId();

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정이 존재하지 않습니다."));

        // 방 멤버 여부 검증
        Long roomId = schedule.getRoom().getId();
        validateRoomMembership(roomId, memberId);

        return schedule;
    }

    // 일정 전체 조회
    @Transactional(readOnly = true)
    public List<GetScheduleResponseDto> getAllSchedules(Long roomId, String month) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getMemberId();
        validateRoomMembership(roomId, memberId);

        // month 파라미터 파싱
        YearMonth yearMonth;
        try {
            yearMonth = YearMonth.parse(month);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("날짜 형식이 올바르지 않습니다. 'yyyy-MM' 형식이어야 합니다.");
        }

        // 연도와 월 추출
        int year = yearMonth.getYear();
        int monthValue = yearMonth.getMonthValue();

        // 해당 월의 일정 조회
        List<Schedule> schedules = scheduleRepository.findSchedulesByRoomIdAndYearAndMonth(roomId, year, monthValue);

        // Schedule 엔티티를 DTO로 변환
        return schedules.stream()
                .map(GetScheduleResponseDto::from)
                .collect(Collectors.toList());
    }

    //일정 상세 조회
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

        // 댓글 목록 조회
        Page<GetScheduleCommentResponseDto> comments = scheduleCommentRepository
                .findByScheduleOrderByCreatedAtDesc(schedule, commentPageable)
                .map(GetScheduleCommentResponseDto::from);

        return GetScheduleDetailResponseDto.from(schedule, comments);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //단일 일정 수정
    @Transactional
    public UpdateScheduleResponseDto updateSingleSchedule(Long scheduleId, UpdateScheduleRequestDto updateScheduleRequestDto) {
        // 인증된 사용자 정보 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getMemberId();

        // 일정 조회
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정이 존재하지 않습니다."));

        // 방 멤버 여부 검증
        Long roomId = schedule.getRoom().getId();
        validateRoomMembership(roomId, memberId);

        // 단일 일정 여부 체크
        if (schedule.getScheduleCycleId() != null) {
            // 반복 일정에서 특정 하루만 수정하려는 경우
            schedule.setScheduleCycleId(null); // 단일 일정으로 변경
            log.info("단일 일정으로 수정됨: scheduleId={}", scheduleId);
        } else {
            // 애초에 단일 일정인 경우
            log.info("이미 단일 일정입니다. 수정이 불필요합니다.");
        }

        // 일정 수정
        schedule.updateDetails(
                updateScheduleRequestDto.getTitle(),
                updateScheduleRequestDto.getDetail(),
                updateScheduleRequestDto.getStartDate(),
                updateScheduleRequestDto.getStartDateTime(),
                updateScheduleRequestDto.getEndDateTime(),
                updateScheduleRequestDto.isRepeatFlag(),
                updateScheduleRequestDto.getRepeatPattern(),
                updateScheduleRequestDto.getDaysOfWeek(),
                updateScheduleRequestDto.getRepeatEndDate()
        );

        scheduleRepository.save(schedule);
        log.info("수정된 단일 일정: {}", schedule);

        return new UpdateScheduleResponseDto(schedule);
    }

    // 전체 일정 수정
    @Transactional
    public List<UpdateAllScheduleResponseDto> updateAllSchedule(Long scheduleCycleId, UpdateAllScheduleRequestDto requestDto) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getMemberId();

        // 스케줄로 방 ID 찾기 전에 해당 스케줄의 생성자인지 확인
        Schedule schedule = scheduleRepository.findFirstByScheduleCycleId(scheduleCycleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 반복 일정이 존재하지 않습니다."));

        if (!schedule.getCreatedBy().getId().equals(memberId)) {
            throw new AccessDeniedException("해당 일정의 생성자만 수정할 수 있습니다.");
        }

        // 해당 스케줄 사이클에 속한 일정들 조회
        List<Schedule> allSchedules = scheduleRepository.findByScheduleCycleId(scheduleCycleId);

        if (allSchedules.isEmpty()) {
            throw new IllegalArgumentException("해당 반복 일정이 존재하지 않습니다.");
        }

        LocalDate originalStartDate = allSchedules.stream()
                .map(existingSchedule -> existingSchedule.getStartDateTime().toLocalDate())
                .min(LocalDate::compareTo)
                .orElseThrow();

        // 스터디룸 멤버 여부 확인
        Long roomId = allSchedules.get(0).getRoom().getId();
        validateRoomMembership(roomId, memberId);

        // 해당 cycleid를 가진 모든 일정 삭제
        scheduleRepository.deleteByScheduleCycleId(scheduleCycleId);
//        // 기존 일정 삭제
//        allSchedules.forEach(existingSchedule -> {
//            log.info("삭제할 scheduleId {}", schedule.getId());
//            scheduleRepository.delete(schedule);
//        });

        // 새로운 요일과 날짜에 따른 일정 생성
        List<DayOfWeek> newDaysOfWeek = DateUtils.parseDaysOfWeek(requestDto.getDaysOfWeek());
        List<Schedule> newSchedules = new ArrayList<>();

        LocalDate current = originalStartDate;

        while (!current.isAfter(requestDto.getRepeatEndDate())) { // 반복종료날짜 초과 시 더이상 일정을 생성하지 않음
            if (newDaysOfWeek.contains(current.getDayOfWeek())) {
                LocalDateTime startDateTime = current.atTime(requestDto.getStartTime());
                LocalDateTime endDateTime = current.atTime(requestDto.getEndTime());

                log.info("day 요일의 값이 제대로 안 들어오네요 = {}", requestDto.getDay());

                // 일정 생성을 위한 DTO 구성
                CreateScheduleRequestDto createDto = CreateScheduleRequestDto.builder()
                        .title(requestDto.getTitle())
                        .detail(requestDto.getDetail())
                        .startDate(current)
                        .day(requestDto.getDay()) // 여기서 'day' 값도 사용
                        .startTime(requestDto.getStartTime())
                        .endTime(requestDto.getEndTime())
                        .roomId(roomId) // roomId는 이미 확인되었으므로 그대로 사용
                        .repeatFlag(requestDto.isRepeatFlag()) // 반복 여부
                        .repeatPattern(requestDto.getRepeatPattern()) // 반복 패턴
                        .daysOfWeek(requestDto.getDaysOfWeek()) // 반복 요일
                        .repeatEndDate(requestDto.getRepeatEndDate()) // 반복 종료 날짜
                        .build();

                // 새로운 일정 생성
                Schedule newSchedule = Schedule.from(createDto, roomRepository.findById(roomId).orElseThrow(), memberRepository.findById(memberId).orElseThrow());
                newSchedule.setStartDateTime(startDateTime);
                newSchedule.setEndDateTime(endDateTime);
                newSchedule.setScheduleCycleId(scheduleCycleId);

                // 새로운 일정 저장
                scheduleRepository.save(newSchedule);
                newSchedules.add(newSchedule);
                log.info("일괄 수정 : 새롭게 생성된 반복 일정 제목 :: {} 시작날짜 :{}", newSchedule.getTitle(), current);
            }
            current = current.plusDays(1);
        }

        // 응답 DTO로 변환
        return newSchedules.stream()
                .map(UpdateAllScheduleResponseDto::new)
                .collect(Collectors.toList());
    }

    // 이후 일정 전체 수정
    @Transactional
    public List<UpdateUpcomingScheduleResponseDto> updateUpcomingSchedules(Long scheduleCycleId, UpdateUpcomingScheduleRequestDto requestDto) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getMemberId();

        // 스터디룸 멤버 검증을 위한 기존 일정 조회
        List<Schedule> existingSchedules = scheduleRepository.findByScheduleCycleId(scheduleCycleId);
        if (existingSchedules.isEmpty()) {
            log.info("해당 반복 일정은 존재하지 않습니다. scheduleCycleId: {}", scheduleCycleId);
            throw new IllegalArgumentException("해당 반복 일정이 존재하지 않습니다.");
        }

        // 첫 번째 일정에서 필요한 정보 추출
        Schedule firstSchedule = existingSchedules.get(0);
        Room room = firstSchedule.getRoom();
        Member member = firstSchedule.getCreatedBy();

        // 스터디룸 멤버 검증
        validateRoomMembership(room.getId(), memberId);

        // 수정 시작 시점 설정
        LocalDateTime modificationStartDateTime = requestDto.getStartDate().atTime(requestDto.getStartTime());

        // 기준날짜 포함 이후 일정들만 필터링하여 삭제
        List<Schedule> schedulesToDelete = existingSchedules.stream()
                .filter(schedule -> !schedule.getStartDateTime().isBefore(modificationStartDateTime))
                .collect(Collectors.toList());

        if (!schedulesToDelete.isEmpty()) {
            log.info("수정 시작일({}) 이후의 일정 {}개를 삭제합니다.",
                    requestDto.getStartDate(), schedulesToDelete.size());
            scheduleRepository.deleteAll(schedulesToDelete);
        }

        // 새로운 반복 일정들을 담을 리스트
        List<UpdateUpcomingScheduleResponseDto> updatedSchedules = new ArrayList<>();

        // 새로운 scheduleCycleId 생성
        Long newScheduleCycleId = generateNewScheduleCycleId();
        log.info("새로운 scheduleCycleId 생성: {}", newScheduleCycleId);

        // 요일 파싱
        List<DayOfWeek> repeatDays = DateUtils.parseDaysOfWeek(requestDto.getDaysOfWeek());
        log.info("반복 요일: {}", repeatDays);

        // 새로운 반복 일정 생성
        LocalDate currentDate = requestDto.getStartDate();
        LocalDate endDate = requestDto.getRepeatEndDate();

        while (!currentDate.isAfter(endDate)) {
            if (repeatDays.contains(currentDate.getDayOfWeek())) {
                LocalDateTime startDateTime = currentDate.atTime(requestDto.getStartTime());
                LocalDateTime endDateTime = currentDate.atTime(requestDto.getEndTime());

                // 일정 생성을 위한 DTO 구성
                CreateScheduleRequestDto createDto = CreateScheduleRequestDto.builder()
                        .title(requestDto.getTitle())
                        .detail(requestDto.getDetail())
                        .startDate(currentDate)
                        .day(requestDto.getDay())
                        .startTime(requestDto.getStartTime())
                        .endTime(requestDto.getEndTime())
                        .roomId(room.getId())
                        .repeatFlag(true)
                        .repeatPattern(requestDto.getRepeatPattern())
                        .daysOfWeek(requestDto.getDaysOfWeek())
                        .repeatEndDate(requestDto.getRepeatEndDate())
                        .build();

                // 새로운 일정 생성
                Schedule newSchedule = Schedule.from(createDto, room, member);
                newSchedule.setStartDateTime(startDateTime);
                newSchedule.setEndDateTime(endDateTime);
                newSchedule.setScheduleCycleId(newScheduleCycleId);

                // 일정 저장
                Schedule savedSchedule = scheduleRepository.save(newSchedule);
                log.info("새로운 일정 생성 완료 - ID: {}, 날짜: {}",
                        savedSchedule.getId(), currentDate);

                // 응답 DTO 추가
                updatedSchedules.add(new UpdateUpcomingScheduleResponseDto(
                        savedSchedule.getId(),
                        savedSchedule.getTitle(),
                        savedSchedule.getStartDate(),
                        savedSchedule.getStartDateTime().toLocalTime(),
                        savedSchedule.getEndDateTime().toLocalTime()
                ));
            }
            currentDate = currentDate.plusDays(1);
        }

        log.info("수정된 반복 일정 생성 완료. 생성된 일정 수: {}", updatedSchedules.size());
        return updatedSchedules;
    }

    // 단일 일정 삭제
    @Transactional
    public void deleteSingleSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정은 존재하지 않습니다."));

        // 단일 일정 삭제
        scheduleRepository.delete(schedule);
    }

    // 반복 일정 일괄 삭제 (단일로 된 일정을 일괄로 삭제하는 게 아님!)
    @Transactional
    public void deleteAllSchedule(Long scheduleCycleId) {
        scheduleRepository.deleteByScheduleCycleId(scheduleCycleId);
    }

    // 반복 일정 중 특정 일자 기준으로 이후 일정 전체 삭제
    @Transactional
    public void deleteUpcomingSchedules(Long scheduleCycleId, UpdateUpcomingScheduleRequestDto requestDto) {
        List<Schedule> existingSchedules = scheduleRepository.findByScheduleCycleId(scheduleCycleId);
        if (existingSchedules.isEmpty()) {
            log.info("해당 반복 일정은 존재하지 않습니다. scheduleCycleId: {}", scheduleCycleId);
            throw new IllegalArgumentException("해당 반복 일정이 존재하지 않습니다.");
        }

        // 요청 받은 시작일을 LocalDateTime으로 변환 (기본 시작 시간 00:00으로 설정)
        LocalDateTime modificationStartDateTime = requestDto.getStartDate().atStartOfDay();

        // 기준 날짜 이후 일정들만 필터링하여 삭제
        List<Schedule> schedulesToDelete = existingSchedules.stream()
                .filter(schedule -> !schedule.getStartDateTime().isBefore(modificationStartDateTime))  // 날짜 비교
                .collect(Collectors.toList());

        if (!schedulesToDelete.isEmpty()) {
            log.info("삭제 시작일({}) 이후의 일정 {}개를 삭제합니다.", requestDto.getStartDate(), schedulesToDelete.size());
            scheduleRepository.deleteAll(schedulesToDelete);  // 해당 일정들 삭제
        } else {
            log.info("삭제 시작일 이후의 일정이 없습니다. 삭제할 일정이 없습니다.");
        }
    }

}
