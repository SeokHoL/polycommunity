package kr.ac.kopo.polycommunity.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String memberId;
    private String memberPassword;
    private String memberName;
    private String memberPhone;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;  // 기본 역할은 USER

    // Builder 패턴 사용
    @Builder
    public Member(String memberId, String memberPassword, String memberName, String memberPhone) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberPhone = memberPhone;
    }
}
