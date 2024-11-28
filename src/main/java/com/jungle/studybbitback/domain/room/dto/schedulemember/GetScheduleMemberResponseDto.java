package com.jungle.studybbitback.domain.room.dto.schedulemember;

import com.jungle.studybbitback.domain.room.entity.schedule.ParticipateStatusEnum;
import com.jungle.studybbitback.domain.room.entity.schedule.ScheduleMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetScheduleMemberResponseDto {
    private Long memberId;
    private String memberNickname;
    private ParticipateStatusEnum participateStatus;
    private String preAbsenceDetail;

    public static GetScheduleMemberResponseDto from(ScheduleMember scheduleMember) {
        return new GetScheduleMemberResponseDto(
                scheduleMember.getMember().getId(),
                scheduleMember.getMember().getNickname(),
                scheduleMember.getParticipateStatus(),
                scheduleMember.getPreAbsenceDetail()
        );
    }
}
