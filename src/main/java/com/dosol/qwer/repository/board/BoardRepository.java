package com.dosol.qwer.repository.board;


import com.dosol.qwer.domain.board.Board;
import com.dosol.qwer.repository.board.search.BoardSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

}


