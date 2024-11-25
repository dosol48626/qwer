package com.dosol.qwer.domain.todo;

import com.dosol.qwer.domain.BaseEntity;
import com.dosol.qwer.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Todo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoNum; // Primary Key 이름 변경

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_num") // User 관계 컬럼 명시
    private User user;

    @NotBlank // 문자열 비어 있음 방지
    @Column(nullable = false, length = 100)
    private String title;

    @NotBlank // 문자열 비어 있음 방지
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Boolean complete;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = true) // nullable 명시
    private LocalDate dueDate;

    // 엔티티 내 메서드에서는 유효성 검사 제거
    public void change(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // @PrePersist는 유지
    @PrePersist
    public void prePersist() {
        this.complete = Boolean.FALSE; // 완료 여부 기본값 설정
    }

}
