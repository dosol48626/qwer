package com.dosol.qwer.repository.board.search;

import com.dosol.qwer.domain.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {

    /**
     * 검색 조건(types)과 키워드(keyword)를 기반으로 페이징된 Board 데이터를 반환합니다.
     *
     * @param types    검색 조건 배열 (예: "title", "content")
     * @param keyword  검색 키워드
     * @param pageable 페이징 및 정렬 정보
     * @return 페이징된 Board 데이터
     */
    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);
}
