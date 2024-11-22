package com.dosol.qwer.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDTO {

    private Long boardNum;

    private String nickname;

    private String title;

    private String content;

    private int visitCount;

    private String createTime;
    private String updateTime;
}
