package com.dosol.qwer.service.todo;

import com.dosol.qwer.dto.todo.PageRequestDTO;
import com.dosol.qwer.dto.todo.PageResponseDTO;
import com.dosol.qwer.dto.todo.TodoDTO;

public interface TodoService {
    // 게시글 목록 조회
    PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO);

    // 게시글 등록
    Long register(TodoDTO todoDTO);

    // 게시글 조회
    TodoDTO readOne(Long TodoNum);

    // 게시글 수정
    void modify(TodoDTO todoDTO);

    // 게시글 삭제
    void remove(Long TodoNum);

}
