package com.jungle.studybbitback.domain.room.dto.schedulemember;

import com.jungle.studybbitback.domain.room.entity.schedule.ScheduleMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateScheduleMemberResponseDto {
    private Long scheduleId;
    private Long scheduleMemberId;
    private Long memberId;
    private Boolean isParticipated;
    private String nickname;

    public static CreateScheduleMemberResponseDto from(ScheduleMember scheduleMember) {
        return new CreateScheduleMemberResponseDto(
                scheduleMember.getSchedule().getId(),
                scheduleMember.getId(),
                scheduleMember.getMember().getId(),
                scheduleMember.getIsParticipated(),
                scheduleMember.getMember().getNickname()
        );
    }
}

