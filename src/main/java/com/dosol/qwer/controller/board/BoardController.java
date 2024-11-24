package com.dosol.qwer.controller.board;

import com.dosol.qwer.config.auth.CustomUserDetails;
import com.dosol.qwer.dto.board.BoardDTO;
import com.dosol.qwer.dto.board.PageRequestDTO;
import com.dosol.qwer.dto.board.PageResponseDTO;
import com.dosol.qwer.service.board.BoardService;
import com.dosol.qwer.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final UserService userService; // UserService 추가

    /**
     * 게시글 목록 페이지
     */
    @GetMapping("/list")
    public String list(PageRequestDTO pageRequestDTO, Model model,
                       @AuthenticationPrincipal UserDetails userDetails) {

        // 닉네임과 게시글 데이터 추가
        model.addAttribute("nickname", userService.getUserNickname((CustomUserDetails) userDetails));
        model.addAttribute("responseDTO", boardService.list(pageRequestDTO));

        return "board/list";
    }

    /**
     * 게시글 등록 페이지
     */
    @GetMapping("/register")
    public String registerGET(
            Model model,
            @AuthenticationPrincipal UserDetails userDetails) {

        // 닉네임 가져오기
        String nickname = userService.getUserNickname((CustomUserDetails) userDetails);
        model.addAttribute("nickname", nickname);

        return "board/register";
    }

    /**
     * 게시글 등록 처리
     */
    @PostMapping("/register")
    public String registerPOST(
            @Valid @ModelAttribute BoardDTO boardDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        if (bindingResult.hasErrors()) {
            String nickname = userService.getUserNickname((CustomUserDetails) userDetails);
            model.addAttribute("nickname", nickname);
            return "board/register";
        }

        // 사용자 닉네임 설정
        String nickname = userService.getUserNickname((CustomUserDetails) userDetails);
        boardDTO.setNickname(nickname);

        // 서비스 호출하여 게시글 등록 처리
        boardService.register(boardDTO);
        return "redirect:/board/list";
    }

    /**
     * 게시글 읽기 페이지
     */
    @GetMapping("/read")
    public String readGET(
            Model model,
            @RequestParam Long boardNum,
            @AuthenticationPrincipal UserDetails userDetails) {

        // 닉네임 가져오기
        String nickname = userService.getUserNickname((CustomUserDetails) userDetails);
        model.addAttribute("nickname", nickname);

        BoardDTO boardDTO = boardService.readOne(boardNum);
        model.addAttribute("boardDTO", boardDTO);
        return "board/read";
    }

    /**
     * 게시글 수정 페이지
     */
    @GetMapping("/modify")
    public String modifyGET(
            Model model,
            @RequestParam Long boardNum,
            @AuthenticationPrincipal UserDetails userDetails) {

        // 닉네임 가져오기
        String nickname = userService.getUserNickname((CustomUserDetails) userDetails);
        model.addAttribute("nickname", nickname);

        BoardDTO boardDTO = boardService.readOne(boardNum);
        model.addAttribute("boardDTO", boardDTO);
        return "board/modify";
    }

    /**
     * 게시글 수정 처리
     */
    @PostMapping("/modify")
    public String modifyPOST(
            @Valid @ModelAttribute BoardDTO boardDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        if (bindingResult.hasErrors()) {
            String nickname = userService.getUserNickname((CustomUserDetails) userDetails);
            model.addAttribute("nickname", nickname);
            return "redirect:/board/modify?boardNum=" + boardDTO.getBoardNum();
        }

        boardService.modify(boardDTO);
        return "redirect:/board/read?boardNum=" + boardDTO.getBoardNum();
    }

    /**
     * 게시글 삭제 처리
     */
    @GetMapping("/remove")
    public String remove(
            @RequestParam Long boardNum,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        // 닉네임 가져오기
        String nickname = userService.getUserNickname((CustomUserDetails) userDetails);
        model.addAttribute("nickname", nickname);

        boardService.remove(boardNum);
        return "redirect:/board/list";
    }
}
