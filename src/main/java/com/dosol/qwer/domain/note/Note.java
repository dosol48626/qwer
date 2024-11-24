package com.dosol.qwer.domain.note;

import com.dosol.qwer.domain.BaseEntity;
import com.dosol.qwer.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Note extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteNum; // Primary Key

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_num", nullable = false) // User 연관 관계 필수
    private User user;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column(nullable = true) // 명시적으로 nullable 설정
    private String content;

    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
