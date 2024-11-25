package com.dosol.qwer.repository.todo.search;

import com.dosol.qwer.domain.todo.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoSearch {
    //Page<Todo> searchAll(String[] types, String keyword, Pageable pageable, String pageType, Long nickname);

    Page<Todo> searchAll(String[] types, String keyword, Pageable pageable, String pageType, String nickname);

    //Page<Todo> searchAll(String[] types, String keyword, Pageable pageable, Long userNum); 이거는 타입이 안나올텐데
}

