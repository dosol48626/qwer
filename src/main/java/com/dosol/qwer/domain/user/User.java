package com.dosol.qwer.domain.user;

import com.dosol.qwer.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNum; // Primary Key

    @Column(unique = true, nullable = false) //중복방지
    private String username; // 로그인 ID

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(unique = true, nullable = false)
    private String nickname; // 닉네임

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // 권한 (USER, ADMIN)

    @Column(nullable = false)
    private String profileImagePath; // 프로필 이미지 파일 경로
}
