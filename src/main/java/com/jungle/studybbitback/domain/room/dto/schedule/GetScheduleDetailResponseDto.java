package com.jungle.studybbitback.domain.room.dto.schedule;

import com.jungle.studybbitback.domain.room.dto.schedulemember.GetScheduleMemberResponseDto;
import com.jungle.studybbitback.domain.room.entity.schedule.Schedule;
import com.jungle.studybbitback.domain.room.entity.schedule.ScheduleMember;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetScheduleDetailResponseDto {
    private Long scheduleId;
    private String title;
    private LocalDateTime scheduleDateTime;
    private String detail;
    private Long roomId;
    private String creatorName;
    private List<GetScheduleMemberResponseDto> participantStatuses;

    public static GetScheduleDetailResponseDto from(Schedule schedule, List<ScheduleMember> scheduleMembers) {
        // ScheduleMember 목록을 GetScheduleMemberResponseDto 목록으로 변환
        List<GetScheduleMemberResponseDto> participantStatuses = scheduleMembers.stream()
                .map(GetScheduleMemberResponseDto::from)
                .collect(Collectors.toList());

        return GetScheduleDetailResponseDto.builder()
                .scheduleId(schedule.getId())
                .title(schedule.getTitle())
                .scheduleDateTime(schedule.getScheduleDateTime())
                .detail(schedule.getDetail())
                .roomId(schedule.getRoom().getId())
                .creatorName(schedule.getCreatedBy().getNickname())
                .participantStatuses(participantStatuses)  // 참석 상태 목록
                .build();
    }


}
