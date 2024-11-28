package com.jungle.studybbitback.domain.room.dto.schedulecomment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateScheduleCommentRequestDto {
    private Long scheduleId;
    private String content;
}
