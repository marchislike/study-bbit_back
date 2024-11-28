package com.jungle.studybbitback.domain.room.entity.schedule;

import com.jungle.studybbitback.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor
public class ScheduleMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_member_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    //@Column(nullable = false)
    private ParticipateStatusEnum participateStatus;

    private String preAbsenceDetail;

    public ScheduleMember(Schedule schedule, Member member, ParticipateStatusEnum status, String detail) {
        this.schedule = schedule;
        this.member = member;
        this.participateStatus = status;
        this.preAbsenceDetail = detail;
    }

    public ScheduleMember(Schedule schedule, Member member, ParticipateStatusEnum status) {
        this.schedule = schedule;
        this.member = member;
        this.participateStatus = status;
    }
}
