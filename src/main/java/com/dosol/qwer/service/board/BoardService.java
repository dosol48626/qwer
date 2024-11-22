package com.dosol.qwer.service.board;

import com.dosol.qwer.dto.board.BoardDTO;

import java.util.List;

public interface BoardService {
    List<BoardDTO> readAll();

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long boardId);

    void modify(BoardDTO boardDTO);

    void remove(Long boardId);
}
