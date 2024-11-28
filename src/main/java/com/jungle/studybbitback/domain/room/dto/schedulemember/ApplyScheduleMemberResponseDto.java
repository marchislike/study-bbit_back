package com.jungle.studybbitback.domain.room.dto.schedulemember;

import com.jungle.studybbitback.domain.room.entity.schedule.ParticipateStatusEnum;
import com.jungle.studybbitback.domain.room.entity.schedule.ScheduleMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplyScheduleMemberResponseDto {
    private Long memberId;
    private String memberNickname;
    private ParticipateStatusEnum participateStatus;

    public static ApplyScheduleMemberResponseDto from(ScheduleMember scheduleMember) {
        return new ApplyScheduleMemberResponseDto(
                scheduleMember.getMember().getId(),
                scheduleMember.getMember().getNickname(),
                scheduleMember.getParticipateStatus()
        );
    }
}

