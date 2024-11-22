package com.dosol.qwer.dto.board;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    private int page = 1; // 현재 페이지 번호

    @Builder.Default
    private int size = 7; // 한 페이지당 데이터 크기

    private String type; // 검색 조건 (예: "title,content")

    private String keyword; // 검색 키워드

    public String[] getTypes() {
        if (type == null || type.isEmpty()) {
            return null;
        }
        return type.split(",");
    }

    public Pageable getPageable(String... props) {
        return PageRequest.of(
                this.page - 1,
                this.size,
                Sort.by(props).descending() // 정렬 방식 고정: 내림차순
        );
    }

    public String getLink() {
        StringBuilder builder = new StringBuilder();

        builder.append("page=").append(this.page);
        builder.append("&size=").append(this.size);

        if (type != null && !type.isEmpty()) {
            builder.append("&type=").append(type);
        }

        if (keyword != null && !keyword.isEmpty()) {
            try {
                builder.append("&keyword=").append(URLEncoder.encode(keyword, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Encoding error while generating link", e); // 예외 처리 개선
            }
        }

        return builder.toString();
    }
}
