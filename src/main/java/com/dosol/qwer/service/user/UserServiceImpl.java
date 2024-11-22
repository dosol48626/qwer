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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public void registerUser(UserDTO userDTO, MultipartFile profileImage) {
        // 중복된 username 확인
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        // 중복된 nickname 확인
        if (userRepository.existsByNickname(userDTO.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // 프로필 이미지 저장
        if (profileImage == null || profileImage.isEmpty()) {
            throw new IllegalArgumentException("프로필 이미지는 필수입니다.");
        }
        String uploadDir = "C:/upload/profile_images"; // 파일 저장 경로
        String uniqueFilename = saveProfileImage(profileImage, uploadDir);
        userDTO.setProfileImagePath(uniqueFilename); // DTO에 경로 설정

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

    @Override
    public String getUserProfileImage(CustomUserDetails userDetails) {
        if (userDetails != null) {
            return userDetails.getProfileImagePath(); // User 엔터티의 프로필 이미지 경로 반환
        }
        return null; // 비로그인 상태
    }

    private String saveProfileImage(MultipartFile profileImage, String uploadDir) {
        try {
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs(); // 디렉토리 생성
            }

            String originalFilename = profileImage.getOriginalFilename();
            String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;
            File destinationFile = new File(uploadDir, uniqueFilename);
            profileImage.transferTo(destinationFile); // 파일 저장

            return uniqueFilename; // 저장된 파일 이름 반환
        } catch (IOException e) {
            throw new RuntimeException("프로필 이미지 저장 중 오류가 발생했습니다.", e);
        }
    }
}