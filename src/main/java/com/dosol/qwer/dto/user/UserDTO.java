package com.dosol.qwer.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long userNum;       // Primary Key
    private String username;    // 로그인 ID
    private String password;
    private String nickname;    // 닉네임
    private String role;        // 권한 (ENUM 타입 -> String 변환)
}