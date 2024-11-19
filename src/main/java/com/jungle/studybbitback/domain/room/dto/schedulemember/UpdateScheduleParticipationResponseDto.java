package com.jungle.studybbitback.domain.room.dto.schedulemember;

import com.jungle.studybbitback.domain.room.entity.schedule.ScheduleMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateScheduleParticipationResponseDto {
    private Long scheduleId;
    private Long scheduleMemberId;
    private Long memberId;
    private Boolean isParticipated; // true : 참석, false : 불참석, null : 무응답
    private String nickname;

    public static UpdateScheduleParticipationResponseDto from(ScheduleMember scheduleMember) {
        return new UpdateScheduleParticipationResponseDto(
                scheduleMember.getSchedule().getId(),
                scheduleMember.getId(),
                scheduleMember.getMember().getId(),
                scheduleMember.getIsParticipated(),
                scheduleMember.getMember().getNickname()
        );
    }
}
