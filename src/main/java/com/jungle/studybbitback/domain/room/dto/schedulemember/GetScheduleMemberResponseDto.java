package com.jungle.studybbitback.domain.room.dto.schedulemember;

import com.jungle.studybbitback.domain.room.entity.schedule.ParticipateStatusEnum;
import com.jungle.studybbitback.domain.room.entity.schedule.ScheduleMember;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetScheduleMemberResponseDto {
    private Long memberId;
    private String memberNickname;
    private ParticipateStatusEnum participateStatus;
    private String preAbsenceDetail;

    public static GetScheduleMemberResponseDto from(ScheduleMember scheduleMember) {
        return GetScheduleMemberResponseDto.builder()
                .memberId(scheduleMember.getMember().getId())
                .memberNickname(scheduleMember.getMember().getNickname())
                .participateStatus(scheduleMember.getParticipateStatus())
                .preAbsenceDetail(scheduleMember.getPreAbsenceDetail())
                .build();
    }
}
