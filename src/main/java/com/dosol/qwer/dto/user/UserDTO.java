package com.dosol.qwer.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long userNum;       // Primary Key
    private String username;    // 로그인 ID
    private String password;    // 비밀번호
    private String nickname;    // 닉네임
    private String role;        // 권한 (ENUM 타입 -> String 변환)
    private String profileImagePath; // 프로필 이미지 파일 경로
}