package com.dosol.qwer.repository.note.search.search;

import com.dosol.qwer.domain.note.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoteSearch { // 인터페이스 이름도 NoteSearch로 수정

    /**
     * 특정 사용자의 노트를 검색 조건(types)과 키워드(keyword)를 기반으로 페이징 처리하여 조회
     *
     * @param types    검색 조건 배열 (예: "title", "content")
     * @param keyword  검색 키워드
     * @param pageable 페이징 정보
     * @param userNum  사용자 번호 (해당 사용자의 노트를 필터링)
     * @return 페이징된 Note 데이터
     */
    Page<Note> searchAll(String[] types, String keyword, Pageable pageable, Long userNum);
}
