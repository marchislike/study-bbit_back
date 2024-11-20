package com.jungle.studybbitback.domain.room.dto.schedulemember;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateScheduleMemberRequestDto {
    private Long scheduleId;
    private Boolean isParticipated;
}
