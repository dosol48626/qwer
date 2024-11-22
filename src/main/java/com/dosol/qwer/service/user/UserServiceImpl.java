package com.dosol.qwer.service.user;

import com.dosol.qwer.config.auth.CustomUserDetails;
import com.dosol.qwer.domain.user.Role;
import com.dosol.qwer.domain.user.User;
import com.dosol.qwer.dto.user.UserDTO;
import com.dosol.qwer.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public void registerUser(UserDTO userDTO) {
        // 중복된 username 확인
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        // 비밀번호 암호화
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // DTO -> 엔터티 변환
        User user = modelMapper.map(userDTO, User.class);
        user.setRole(Role.USER); // 기본 권한 설정

        // 데이터베이스 저장
        userRepository.save(user);
    }

    @Override
    public String getUserNickname(CustomUserDetails userDetails) {
        if (userDetails != null) {
            return userDetails.getNickname();
        }
        return null; // 비로그인 상태
    }
}
