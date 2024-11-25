package com.dosol.qwer.controller.todo;

import com.dosol.qwer.dto.todo.TodoDTO;
import com.dosol.qwer.dto.todo.PageRequestDTO;
import com.dosol.qwer.service.todo.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;

    /**
     * 할 일 목록 페이지
     */
    @GetMapping("/list")
    public String list(PageRequestDTO pageRequestDTO, Model model) {
        model.addAttribute("responseDTO", todoService.list(pageRequestDTO));
        return "todo/list";
    }

    /**
     * 할 일 작성 페이지
     */
    @GetMapping("/register")
    public String registerGET() {
        return "todo/register";
    }

    /**
     * 할 일 작성 처리
     */
    @PostMapping("/register")
    public String registerPOST(@Valid @ModelAttribute TodoDTO todoDTO,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "todo/register";
        }

        todoService.register(todoDTO);
        return "redirect:/todo/list";
    }

    /**
     * 할 일 읽기 페이지
     */
    @GetMapping("/read")
    public String readGET(@RequestParam Long todoNum, Model model) {
        model.addAttribute("todoDTO", todoService.readOne(todoNum));
        return "todo/read";
    }

    /**
     * 할 일 수정 페이지
     */
    @GetMapping("/modify")
    public String modifyGET(@RequestParam Long todoNum, Model model) {
        model.addAttribute("todoDTO", todoService.readOne(todoNum));
        return "todo/modify";
    }

    /**
     * 할 일 수정 처리
     */
    @PostMapping("/modify")
    public String modifyPOST(@Valid @ModelAttribute TodoDTO todoDTO,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/todo/modify?todoNum=" + todoDTO.getTodoNum();
        }

        todoService.modify(todoDTO);
        return "redirect:/todo/read?todoNum=" + todoDTO.getTodoNum();
    }

    /**
     * 할 일 삭제 처리
     */
    @GetMapping("/remove")
    public String remove(@RequestParam Long todoNum) {
        todoService.remove(todoNum);
        return "redirect:/todo/list";
    }
}
