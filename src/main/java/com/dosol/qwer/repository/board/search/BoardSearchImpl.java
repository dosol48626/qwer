package com.dosol.qwer.repository.board.search;

import com.dosol.qwer.domain.board.Board;
import com.dosol.qwer.domain.board.QBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;

        // 기본 쿼리 작성
        JPQLQuery<Board> query = from(board);

        // 연관된 user와 join 처리
        query.leftJoin(board.user).fetchJoin();

        // 검색 조건 처리
        if ((types != null && types.length > 0) && keyword != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.containsIgnoreCase(keyword)); // 제목 검색
                        break;
                    case "c":
                        booleanBuilder.or(board.content.containsIgnoreCase(keyword)); // 내용 검색
                        break;
                    case "w":
                        booleanBuilder.or(board.user.nickname.containsIgnoreCase(keyword)); // 작성자 닉네임 검색
                        break;
                    default:
                        break;
                }
            }
            query.where(booleanBuilder);
        }

        // 기본 조건 추가
        query.where(board.boardNum.gt(0L)); // Primary Key 조건

        // 페이징 처리 적용
        this.getQuerydsl().applyPagination(pageable, query);

        // 결과 데이터 조회
        List<Board> list = query.fetch();

        // 전체 데이터 개수 조회
        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }
}
