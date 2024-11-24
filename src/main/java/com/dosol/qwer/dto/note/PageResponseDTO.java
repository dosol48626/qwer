package com.dosol.qwer.dto.note;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> {

    private int page;      // 현재 페이지 번호
    private int size;      // 한 페이지당 데이터 개수
    private int total;     // 전체 데이터 수

    private int start;     // 시작 페이지 번호
    private int end;       // 끝 페이지 번호

    private boolean prev;  // 이전 페이지 존재 여부
    private boolean next;  // 다음 페이지 존재 여부

    private List<E> dtoList; // 데이터 목록

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total) {

        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        if (total <= 0) { // 데이터가 없는 경우 초기값 설정
            this.start = 0;
            this.end = 0;
            this.prev = false;
            this.next = false;
            return;
        }

        // 끝 페이지 계산
        this.end = (int) Math.ceil((double) this.page / 10) * 10;

        // 시작 페이지 계산
        this.start = this.end - 9;

        // 마지막 페이지 계산
        int lastPage = (int) Math.ceil((double) total / this.size);

        // 끝 페이지가 마지막 페이지보다 크면 조정
        if (this.end > lastPage) {
            this.end = lastPage;
        }

        // 이전 페이지 여부
        this.prev = this.start > 1;

        // 다음 페이지 여부
        this.next = this.total > this.end * this.size;
    }
}
