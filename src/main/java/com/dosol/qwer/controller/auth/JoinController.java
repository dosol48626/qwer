package com.dosol.qwer.controller.auth;

import com.dosol.qwer.dto.user.UserDTO;
import com.dosol.qwer.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class JoinController {

    private final UserService userService;
    /**
     * 회원가입 페이지 이동
     */
    @GetMapping("/join")
    public String showJoinPage() {
        return "/join";
    }

    /**
     * 회원가입 처리
     */
    @PostMapping("/join")
    public String processJoin(@ModelAttribute UserDTO userDTO,
                              @RequestParam("profileImage") MultipartFile profileImage,
                              Model model) {
        try {
            // 회원가입 로직 호출
            userService.registerUser(userDTO, profileImage);
            return "redirect:/login"; // 회원가입 성공 후 로그인 페이지로 이동
        } catch (IllegalArgumentException e) {
            // 에러 메시지를 모델에 추가
            model.addAttribute("errorMessage", e.getMessage());
            return "/join"; // 회원가입 페이지로 다시 이동
        }
    }
}
