package com.dosol.qwer.dto.todo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {


    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 4;

    private String type;
    private String keyword;

    //페이지 유형 (all, today, upcoming)
    private String pageType;
    private LocalDate dueDate;

    public String[] getTypes(){
        if(type == null || type.isEmpty()){
            return null;
        }
        return type.split("");
    }

    public Pageable getPageable(String... props) {
        return PageRequest.of(
                this.page - 1,
                this.size,
                Sort.by(props).descending() // 기본 정렬 방식: 내림차순
        );
    }

    private String link;
    public String getLink() {

        if(link == null){
            StringBuilder builder = new StringBuilder();

            builder.append("page=" + this.page);

            builder.append("&size=" + this.size);


            if(type != null && type.length() > 0){
                builder.append("&type=" + type);
            }

            if(keyword != null){
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword,"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                }
            }

            //pageType 파라미터 추가
            if(pageType != null){
                builder.append("&pageType=" + pageType);
            }
            link = builder.toString();
        }

        return link;
    }



}