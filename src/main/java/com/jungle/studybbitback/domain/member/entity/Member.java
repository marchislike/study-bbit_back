package com.jungle.studybbitback.domain.member.entity;

import com.jungle.studybbitback.common.entity.ModifiedTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public Member(String email, String password, String nickname, MemberRoleEnum role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public Member(Long memberId, String email, String password, String nickname, MemberRoleEnum role) {
        this.id = memberId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }
}
