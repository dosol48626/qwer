package com.dosol.qwer.dto.todo;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoDTO {

    private Long todoNum;

    private String nickname;

    @NotEmpty(message = "제목은 필수 항목입니다.")
    private String title; // 제목

    @NotEmpty(message = "내용은 필수 항목입니다.")
    private String description; // 설명

    private Boolean complete; // 완료 여부

    private LocalDate dueDate; // 마감일

    private LocalDateTime createTime; // 등록일

    private LocalDateTime updateTime;
}
