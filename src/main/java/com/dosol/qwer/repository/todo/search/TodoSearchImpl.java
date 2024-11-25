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
    public Page<Todo> searchAll(String[] types, String keyword, Pageable pageable, String pageType, String nickname) {
        QTodo todo = QTodo.todo; // QueryDSL을 사용하는 Todo 객체
        JPQLQuery<Todo> query = from(todo);

        // 1. 로그인된 사용자 필터링 (nickname 기준)
        query.where(todo.user.nickname.eq(nickname)); // 로그인된 사용자 필터링 추가

        // 2. 검색 조건 처리
        if (types != null && types.length > 0 && keyword != null) { // types와 keyword가 null이 아닐 때만 처리
            BooleanBuilder builder = new BooleanBuilder();
            for (String type : types) {
                switch (type) {
                    case "t":
                        builder.or(todo.title.contains(keyword));
                        break;
                    case "d":
                        builder.or(todo.description.contains(keyword));
                        break;
                }
            }
            query.where(builder); // 검색 조건 추가
        }

        // 3. pageType 처리
        if (pageType != null) { // pageType이 null이 아닐 때만 처리
            switch (pageType) {
                case "past":
                    query.where(todo.dueDate.lt(LocalDate.now())); // 오늘 이전 일정
                    break;
                case "today":
                    query.where(todo.dueDate.eq(LocalDate.now())); // 오늘 일정
                    break;
                case "upcoming":
                    query.where(todo.dueDate.gt(LocalDate.now()).or(todo.dueDate.isNull())); // 미래 일정
                    break;
                case "completed":
                    query.where(todo.complete.eq(true)); // 완료된 일정
                    break;
                case "list":
                    // 기본적으로 모든 Todo 항목
                    break; // 추가 조건 없음
            }
        }

        // 4. 페이징 및 결과 조회
        this.getQuerydsl().applyPagination(pageable, query); // 페이징 적용
        List<Todo> list = query.fetch(); // 실제 데이터 조회
        long total = query.fetchCount(); // 총 개수 계산

        return new PageImpl<>(list, pageable, total); // 페이징 결과 반환
    }


}

