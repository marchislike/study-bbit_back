package com.jungle.studybbitback.domain.room.dto.schedulemember;

import com.jungle.studybbitback.domain.room.entity.schedule.ScheduleMember;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetScheduleMemberResponseDto {
    private Long scheduleId;
    private Long memberId;
    private String nickname;
    private Boolean isParticipated;

    public static GetScheduleMemberResponseDto from(ScheduleMember scheduleMember) {
        return GetScheduleMemberResponseDto.builder()
                .scheduleId(scheduleMember.getSchedule().getId())
                .memberId(scheduleMember.getMember().getId())
                .nickname(scheduleMember.getMember().getNickname())
                .isParticipated(scheduleMember.getIsParticipated())
                .build();
    }
}
