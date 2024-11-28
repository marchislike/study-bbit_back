package com.jungle.studybbitback.domain.room.service.schedule;

import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.member.repository.MemberRepository;
import com.jungle.studybbitback.domain.room.dto.schedulecomment.*;
import com.jungle.studybbitback.domain.room.entity.schedule.Schedule;
import com.jungle.studybbitback.domain.room.entity.schedule.ScheduleComment;
import com.jungle.studybbitback.domain.room.respository.RoomMemberRepository;
import com.jungle.studybbitback.domain.room.respository.schedule.ScheduleCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleCommentService {

    private final ScheduleCommentRepository scheduleCommentRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final MemberRepository memberRepository;
    private final ScheduleService scheduleService;

    private void validateRoomMembership(Long roomId, Long memberId) {
        if (roomMemberRepository.findByRoomIdAndMemberId(roomId, memberId).isEmpty()) {
            throw new AccessDeniedException("해당 스터디룸에 가입된 사용자만 접근할 수 있습니다.");
        }
    }

    // 댓글 생성
    @Transactional
    public CreateScheduleCommentResponseDto createComment(Long memberId, CreateScheduleCommentRequestDto requestDto) {
        // 멤버 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        // 일정 조회
        Schedule schedule = scheduleService.getSchedule(requestDto.getScheduleId());

        // 스터디원 검증
        validateRoomMembership(schedule.getRoom().getId(), memberId);

        // 댓글 생성
        ScheduleComment comment = ScheduleComment.of(
                requestDto.getContent(),
                schedule,
                member
        );

        ScheduleComment savedComment = scheduleCommentRepository.save(comment);
        return CreateScheduleCommentResponseDto.from(savedComment);
    }

    // 댓글 조회
    @Transactional
    public Page<GetScheduleCommentResponseDto> getCommentsBySchedule(Long scheduleId, Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        Schedule schedule = scheduleService.getSchedule(scheduleId);

        // 스터디원 검증
        validateRoomMembership(schedule.getRoom().getId(), memberId);

        return scheduleCommentRepository.findByScheduleOrderByCreatedAtDesc(schedule, pageable)
                .map(GetScheduleCommentResponseDto::from);
    }

    // 댓글 수정
    @Transactional
    public UpdateScheduleCommentResponseDto updateComment(Long scheduleCommentId, UpdateScheduleCommentRequestDto requestDto, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        ScheduleComment comment = scheduleCommentRepository.findById(scheduleCommentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        // 스터디원 검증
        validateRoomMembership(comment.getSchedule().getRoom().getId(), memberId);

        // 댓글 작성자 검증
        if (!comment.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("댓글 수정 권한이 없습니다.");
        }

        comment.updateContent(requestDto.getContent());
        return UpdateScheduleCommentResponseDto.from(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long scheduleCommentId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        ScheduleComment comment = scheduleCommentRepository.findById(scheduleCommentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        // 스터디원 검증
        validateRoomMembership(comment.getSchedule().getRoom().getId(), memberId);

        // 댓글 작성자 검증
        if (!comment.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
        }

        scheduleCommentRepository.delete(comment);
    }


}
