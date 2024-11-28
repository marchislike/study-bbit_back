package com.jungle.studybbitback.domain.room.dto.schedulecomment;

import com.jungle.studybbitback.domain.room.entity.schedule.ScheduleComment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UpdateScheduleCommentResponseDto {
    private Long scheduleId;
    private Long scheduleCommentId;
    private String content;
    private Long memberId;
    private String memberNickname;
    private String profileImageUrl;
    private LocalDateTime modifiedAt;

    public static UpdateScheduleCommentResponseDto from(ScheduleComment comment) {
        return UpdateScheduleCommentResponseDto.builder()
                .scheduleId(comment.getSchedule().getId())
                .scheduleCommentId(comment.getId())
                .content(comment.getContent())
                .memberId(comment.getMember().getId())
                .memberNickname(comment.getMember().getNickname())
                .profileImageUrl(comment.getMember().getProfileImageUrl())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }
}
