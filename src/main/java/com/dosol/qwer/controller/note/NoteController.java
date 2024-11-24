package com.dosol.qwer.controller.note;

import com.dosol.qwer.config.auth.CustomUserDetails;
import com.dosol.qwer.dto.note.NoteDTO;
import com.dosol.qwer.dto.note.PageRequestDTO;
import com.dosol.qwer.dto.note.PageResponseDTO;
import com.dosol.qwer.service.note.NoteService;
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
@RequestMapping("/note")
public class NoteController {

    private final NoteService noteService;

    /**
     * 노트 목록 페이지
     */
    @GetMapping("/list")
    public String list(PageRequestDTO pageRequestDTO, Model model,
                       @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 닉네임과 노트 데이터 추가
        model.addAttribute("nickname", userDetails.getNickname());
        model.addAttribute("responseDTO", noteService.list(pageRequestDTO));

        return "note/list";
    }

    /**
     * 노트 작성 페이지
     */
    @GetMapping("/register")
    public String registerGET(Model model,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 닉네임 가져오기
        model.addAttribute("nickname", userDetails.getNickname());
        return "note/register";
    }

    /**
     * 노트 작성 처리
     */
    @PostMapping("/register")
    public String registerPOST(
            @Valid @ModelAttribute NoteDTO noteDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("nickname", userDetails.getNickname());
            return "note/register";
        }

        // 노트 등록 처리
        noteService.register(noteDTO);
        return "redirect:/note/list";
    }

    /**
     * 노트 읽기 페이지
     */
    @GetMapping("/read")
    public String readGET(@RequestParam Long noteNum, Model model,
                          @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 닉네임 가져오기
        model.addAttribute("nickname", userDetails.getNickname());

        // 노트 읽기 처리
        NoteDTO noteDTO = noteService.readOne(noteNum);
        model.addAttribute("noteDTO", noteDTO);

        return "note/read";
    }

    /**
     * 노트 수정 페이지
     */
    @GetMapping("/modify")
    public String modifyGET(@RequestParam Long noteNum, Model model,
                            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 닉네임 가져오기
        model.addAttribute("nickname", userDetails.getNickname());

        // 노트 읽기 처리
        NoteDTO noteDTO = noteService.readOne(noteNum);
        model.addAttribute("noteDTO", noteDTO);

        return "note/modify";
    }

    /**
     * 노트 수정 처리
     */
    @PostMapping("/modify")
    public String modifyPOST(
            @Valid @ModelAttribute NoteDTO noteDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("nickname", userDetails.getNickname());
            return "redirect:/note/modify?noteNum=" + noteDTO.getNoteNum();
        }

        // 노트 수정 처리
        noteService.modify(noteDTO);
        return "redirect:/note/read?noteNum=" + noteDTO.getNoteNum();
    }

    /**
     * 노트 삭제 처리
     */
    @GetMapping("/remove")
    public String remove(@RequestParam Long noteNum,
                         @AuthenticationPrincipal CustomUserDetails userDetails,
                         Model model) {

        // 닉네임 가져오기
        model.addAttribute("nickname", userDetails.getNickname());

        // 노트 삭제 처리
        noteService.remove(noteNum);
        return "redirect:/note/list";
    }
}
