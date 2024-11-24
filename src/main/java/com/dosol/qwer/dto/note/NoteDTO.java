package com.dosol.qwer.dto.note;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoteDTO { // 클래스 이름 변경

    private Long noteNum; // Primary Key 이름 변경

    private String title; // 노트 제목

    private String content; // 노트 내용

    private String nickname; // 작성자 닉네임

    private LocalDateTime createTime; // 등록일

    private LocalDateTime updateTime;
}
