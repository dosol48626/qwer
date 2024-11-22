package com.dosol.qwer.controller.auth;

import com.dosol.qwer.dto.user.UserDTO;
import com.dosol.qwer.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String processJoin(UserDTO userDTO, Model model) {
        try {
            userService.registerUser(userDTO); // 회원가입 로직 호출
            return "redirect:/login"; // 회원가입 성공 후 로그인 페이지로 이동
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage()); // 중복 에러 메시지 전달
            return "/join"; // 회원가입 페이지로 다시 이동
        }
    }
}
