package com.jungle.studybbitback.domain.room.dto.schedulemember;

import com.jungle.studybbitback.domain.room.entity.schedule.ScheduleMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateScheduleParticipationRequestDto {

    private Long scheduleId;
    private Boolean isParticipated;

}
