package com.dosol.qwer.service.todo;

import com.dosol.qwer.config.auth.CustomUserDetails;
import com.dosol.qwer.domain.todo.Todo;
import com.dosol.qwer.domain.user.User;
import com.dosol.qwer.dto.todo.PageRequestDTO;
import com.dosol.qwer.dto.todo.PageResponseDTO;
import com.dosol.qwer.dto.todo.TodoDTO;
import com.dosol.qwer.repository.todo.TodoRepository;
import com.dosol.qwer.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // 로그인된 사용자의 목록 조회
    @Override
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO) {
        User currentUser = getCurrentUser();

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        String pageType = pageRequestDTO.getPageType();
        Pageable pageable = pageRequestDTO.getPageable("todoNum");

        // 로그인된 사용자의 Todo 검색
        Page<Todo> result = todoRepository.searchAll(types, keyword, pageable, pageType, currentUser.getUserNum());

        List<TodoDTO> dtoList = result.getContent().stream()
                .map(todo -> modelMapper.map(todo, TodoDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<TodoDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }

    // 로그인된 사용자의 Todo 등록
    @Override
    public Long register(TodoDTO todoDTO) {
        User currentUser = getCurrentUser();

        Todo todo = modelMapper.map(todoDTO, Todo.class);
        todo.setUser(currentUser); // 로그인된 사용자 설정

        Todo savedTodo = todoRepository.save(todo);
        return savedTodo.getTodoNum();
    }

    // 로그인된 사용자의 Todo 조회
    @Override
    public TodoDTO readOne(Long todoNum) {
        Todo todo = todoRepository.findById(todoNum)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found: " + todoNum));

        User currentUser = getCurrentUser();
        if (!todo.getUser().equals(currentUser)) {
            throw new SecurityException("You are not authorized to view this Todo");
        }

        return modelMapper.map(todo, TodoDTO.class);
    }

    // 로그인된 사용자의 Todo 수정
    @Override
    public void modify(TodoDTO todoDTO) {
        Todo todo = todoRepository.findById(todoDTO.getTodoNum())
                .orElseThrow(() -> new IllegalArgumentException("Todo not found: " + todoDTO.getTodoNum()));

        User currentUser = getCurrentUser();
        if (!todo.getUser().equals(currentUser)) {
            throw new SecurityException("You are not authorized to modify this Todo");
        }

        todo.setTitle(todoDTO.getTitle());
        todo.setDescription(todoDTO.getDescription());
        todo.setDueDate(todoDTO.getDueDate());
        todo.setComplete(todoDTO.getComplete() != null ? todoDTO.getComplete() : false);

        todoRepository.save(todo);
    }

    // 로그인된 사용자의 Todo 삭제
    @Override
    public void remove(Long todoNum) {
        Todo todo = todoRepository.findById(todoNum)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found: " + todoNum));

        User currentUser = getCurrentUser();
        if (!todo.getUser().equals(currentUser)) {
            throw new SecurityException("You are not authorized to delete this Todo");
        }

        todoRepository.delete(todo);
    }

    // 현재 로그인된 사용자 반환
    private User getCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByNickname(userDetails.getNickname())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
