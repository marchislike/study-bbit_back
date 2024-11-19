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

    @Column(nullable = false)
    private Boolean isParticipated; // boolean 대신 Boolean으로 참/불참 외 null(무응답)도 넘길 수 있게 설정

    public ScheduleMember(Schedule schedule, Member member, boolean isParticipated) {
        this.schedule = schedule;
        this.member = member;
        this.isParticipated = isParticipated;
    }
}
