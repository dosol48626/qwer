package com.dosol.qwer.service.user;

import com.dosol.qwer.config.auth.CustomUserDetails;
import com.dosol.qwer.dto.user.UserDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    void registerUser(UserDTO userDTO, MultipartFile profileImage); // 회원가입 처리 메서드

    String getUserNickname(CustomUserDetails userDetails);

    String getUserProfileImage(CustomUserDetails userDetails);
}