package com.dosol.qwer.repository.todo.search;

import com.dosol.qwer.domain.todo.QTodo;
import com.dosol.qwer.domain.todo.Todo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.util.List;

public class TodoSearchImpl extends QuerydslRepositorySupport implements TodoSearch {

    public TodoSearchImpl() {
        super(Todo.class);
    }

    @Override
    public Page<Todo> searchAll(String[] types, String keyword, Pageable pageable, String pageType, Long userNum) {
        QTodo todo = QTodo.todo;
        JPQLQuery<Todo> query = from(todo);

        // keyword가 null인지 확인
        if (types != null && types.length > 0) {
            BooleanBuilder builder = new BooleanBuilder();
            for (String type : types) {
                switch (type) {
                    case "t":
                        if (keyword != null) { // keyword가 null이 아닐 때만 처리
                            builder.or(todo.title.contains(keyword));
                        }
                        break;
                    case "d":
                        if (keyword != null) { // keyword가 null이 아닐 때만 처리
                            builder.or(todo.description.contains(keyword));
                        }
                        break;
                }
            }
            query.where(builder);
        }

        // pageType에 따른 조건 추가
        if (pageType != null) { // pageType이 null이 아닐 때만 처리
            switch (pageType) {
                case "past":
                    query.where(todo.dueDate.lt(LocalDate.now())); // 오늘 이전의 dueDate를 가진 Todo
                    break;
                case "today":
                    query.where(todo.dueDate.eq(LocalDate.now()));
                    break;
                case "upcoming":
                    query.where(todo.dueDate.gt(LocalDate.now()).or(todo.dueDate.isNull())); // dueDate가 null인 경우 포함
                    break;

                case "completed":
                    query.where(todo.complete.eq(true)); // complete가 true인 Todo 항목만
                    break;

                case "list":
                    // 기본적으로 모든 Todo 항목
                    break; // 이 경우는 추가적인 조건이 필요 없음
            }
        }

        query.where(todo.todoNum.gt(0L));
        this.getQuerydsl().applyPagination(pageable, query);
        List<Todo> list = query.fetch();
        long total = query.fetchCount();
        return new PageImpl<>(list, pageable, total);
    }

}

