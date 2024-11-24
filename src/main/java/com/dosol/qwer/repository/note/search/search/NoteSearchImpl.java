package com.dosol.qwer.repository.note.search.search;

import com.dosol.qwer.domain.note.Note;
import com.dosol.qwer.domain.note.QNote;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class NoteSearchImpl extends QuerydslRepositorySupport implements NoteSearch {

    public NoteSearchImpl() {
        super(Note.class);
    }

    @Override
    public Page<Note> searchAll(String[] types, String keyword, Pageable pageable, Long userNum) {
        QNote note = QNote.note;

        JPQLQuery<Note> query = from(note);

        // 사용자 필터링
        if (userNum != null) {
            query.where(note.user.userNum.eq(userNum));
        }

        // 검색 조건 처리
        if (types != null && types.length > 0 && keyword != null && !keyword.isBlank()) {
            BooleanBuilder builder = new BooleanBuilder();

            for (String type : types) {
                switch (type) {
                    case "t": // 제목 검색
                        builder.or(note.title.containsIgnoreCase(keyword));
                        break;
                    case "c": // 내용 검색
                        builder.or(note.content.containsIgnoreCase(keyword));
                        break;
                    default:
                        break;
                }
            }

            query.where(builder);
        }

        // 기본 조건 추가
        query.where(note.noteNum.gt(0L)); // 유효한 Primary Key 조건

        // 페이징 처리
        this.getQuerydsl().applyPagination(pageable, query);

        // 데이터 조회
        List<Note> list = query.fetch();

        // 전체 데이터 개수 조회
        long total = query.fetchCount();

        return new PageImpl<>(list, pageable, total);
    }
}
