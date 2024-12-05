package com.jungle.studybbitback.domain.member.entity;

import com.jungle.studybbitback.common.DurationToIntervalConverter;
import com.jungle.studybbitback.common.entity.ModifiedTimeEntity;
import com.jungle.studybbitback.domain.member.dto.DailyGoalRequestDto;
import com.jungle.studybbitback.domain.member.dto.UpdateMemberRequestDto;
import com.jungle.studybbitback.domain.room.entity.RoomMember;
import com.jungle.studybbitback.domain.room.entity.schedule.ParticipateStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class Member extends ModifiedTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;
    
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberRoleEnum role;

    // Member와 연결된 RoomMember를 통해 참여한 Room들을 조회
    @OneToMany(mappedBy = "member")
    private Set<RoomMember> roomMembers = new HashSet<>();

    @Convert(converter = DurationToIntervalConverter.class)
    @Column(name = "daily_goal", columnDefinition = "INTERVAL")
    @ColumnTransformer(write = "CAST(? AS INTERVAL)") // 삽입 시 CAST 적용
    private Duration dailyGoal;

    @Column(precision = 3, scale = 1) // DB의 NUMERIC(3, 1)에 매핑
    private BigDecimal flowTemperature;

    public Member(String email, String password, String nickname, MemberRoleEnum role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.flowTemperature = BigDecimal.valueOf(36.5);
    }

    public Member(Long memberId, String email, String password, String nickname, MemberRoleEnum role) {
        this.id = memberId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public void updateMember(UpdateMemberRequestDto requestDto, String password, String profileUrl) {
        if (StringUtils.hasText(requestDto.getNickname())) {
            this.nickname = requestDto.getNickname();
        }
        if (StringUtils.hasText(password)) {
            this.password = password;
        }
        if(requestDto.isProfileChanged()) {
            this.profileImageUrl = profileUrl;
        }
    }

    public void updateDailyGoal(DailyGoalRequestDto request) {
        this.dailyGoal = request.getDailyGoal();
    }

    public void revertFlowTemperature(ParticipateStatusEnum statusEnum) {
        if (statusEnum == ParticipateStatusEnum.ON_TIME) {
            this.flowTemperature = this.flowTemperature.subtract(BigDecimal.valueOf(0.1)); // 뺄셈
        } else if (this.flowTemperature.compareTo(BigDecimal.valueOf(100)) < 0) { // < 연산
            if (statusEnum == ParticipateStatusEnum.LATE) {
                this.flowTemperature = this.flowTemperature.add(BigDecimal.valueOf(0.5)); // 덧셈
            } else if (statusEnum == ParticipateStatusEnum.ABSENCE) {
                this.flowTemperature = this.flowTemperature.add(BigDecimal.valueOf(2.5)); // 덧셈
            }
        }
    }

    public void updateFlowTemperature(ParticipateStatusEnum statusEnum) {
        if (statusEnum == ParticipateStatusEnum.ON_TIME && this.flowTemperature.compareTo(BigDecimal.valueOf(100)) < 0) {
            this.flowTemperature = this.flowTemperature.add(BigDecimal.valueOf(0.1)); // 덧셈
        } else if (statusEnum == ParticipateStatusEnum.LATE) {
            this.flowTemperature = this.flowTemperature.subtract(BigDecimal.valueOf(0.5)); // 뺄셈
        } else if (statusEnum == ParticipateStatusEnum.ABSENCE) {
            this.flowTemperature = this.flowTemperature.subtract(BigDecimal.valueOf(2.5)); // 뺄셈
        }
    }
}
