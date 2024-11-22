package com.dosol.qwer.controller;

import com.dosol.qwer.config.auth.CustomUserDetails;
import com.dosol.qwer.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;

    @GetMapping("/")
    public String index(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            // 닉네임 가져오기
            String nickname = userService.getUserNickname(userDetails);
            // 프로필 이미지 경로 가져오기
            String profileImagePath = userService.getUserProfileImage(userDetails);

            // 모델에 닉네임과 프로필 이미지 추가
            model.addAttribute("nickname", nickname);
            model.addAttribute("profileImagePath", profileImagePath);

            System.out.println("로그인된 사용자 닉네임: " + nickname);
            System.out.println("로그인된 사용자 프로필 이미지 경로: " + profileImagePath);
        } else {
            System.out.println("로그인되지 않은 상태");
        }

        return "index";
    }
}