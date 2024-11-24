package com.dosol.qwer.service.board;

import com.dosol.qwer.dto.board.BoardDTO;
import com.dosol.qwer.dto.board.PageRequestDTO;
import com.dosol.qwer.dto.board.PageResponseDTO;

import java.util.List;

public interface BoardService {
    // 게시글 목록 조회
    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    // 게시글 등록
    Long register(BoardDTO boardDTO);

    // 게시글 조회
    BoardDTO readOne(Long boardNum);

    // 게시글 수정
    void modify(BoardDTO boardDTO);

    // 게시글 삭제
    void remove(Long boardNum);

}
