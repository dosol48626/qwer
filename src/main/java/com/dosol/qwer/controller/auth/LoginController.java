package com.dosol.qwer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class LoginController {

    /**
     * 로그인 페이지 렌더링
     */
    @GetMapping("/login")
    public String showLoginPage(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("errorMessage", "아이디 또는 비밀번호를 확인하세요.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "성공적으로 로그아웃되었습니다.");
        }
        return "/login"; // login.html로 이동
    }
}