package com.dosol.qwer.repository.todo;

import com.dosol.qwer.domain.todo.Todo;
import com.dosol.qwer.repository.todo.search.TodoSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoSearch {
}
