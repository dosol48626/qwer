package com.dosol.qwer.service.note;


import com.dosol.qwer.dto.note.NoteDTO;
import com.dosol.qwer.dto.note.PageRequestDTO;
import com.dosol.qwer.dto.note.PageResponseDTO;

public interface NoteService {
    // 게시글 목록 조회
    PageResponseDTO<NoteDTO> list(PageRequestDTO pageRequestDTO);

    // 게시글 등록
    Long register(NoteDTO noteDTO);

    // 게시글 조회
    NoteDTO readOne(Long NoteNum);

    // 게시글 수정
    void modify(NoteDTO noteDTO);

    // 게시글 삭제
    void remove(Long NoteNum);
}
