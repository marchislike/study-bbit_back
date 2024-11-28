package com.jungle.studybbitback.domain.room.dto.schedulecomment;

import com.jungle.studybbitback.domain.room.entity.schedule.ScheduleComment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CreateScheduleCommentResponseDto {
    private Long scheduleId;
    private Long scheduleCommentId;
    private String content;
    private Long memberId;
    private String memberNickname;
    private String profileImageUrl;
    private LocalDateTime createdAt;

    public static CreateScheduleCommentResponseDto from(ScheduleComment comment) {
        return CreateScheduleCommentResponseDto.builder()
                .scheduleId(comment.getSchedule().getId())
                .scheduleCommentId(comment.getId())
                .content(comment.getContent())
                .memberId(comment.getMember().getId())
                .memberNickname(comment.getMember().getNickname())
                .profileImageUrl(comment.getMember().getProfileImageUrl())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
