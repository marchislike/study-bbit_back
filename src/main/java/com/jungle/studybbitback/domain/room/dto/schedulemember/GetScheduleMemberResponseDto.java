package com.jungle.studybbitback.domain.room.dto.schedulemember;

import com.jungle.studybbitback.domain.room.entity.schedule.ScheduleMember;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetScheduleMemberResponseDto {
    private Long memberId;
    private String nickname;
    private Boolean isParticipated;

    public static GetScheduleMemberResponseDto from(ScheduleMember scheduleMember) {
        //Boolean isParticipated = scheduleMember.getIsParticipated();
        return GetScheduleMemberResponseDto.builder()
                .memberId(scheduleMember.getMember().getId())
                .nickname(scheduleMember.getMember().getNickname())
                .isParticipated(scheduleMember.getIsParticipated())
                .build();
    }
}
