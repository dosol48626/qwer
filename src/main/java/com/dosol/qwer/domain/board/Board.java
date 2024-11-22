package com.dosol.qwer.domain.board;

import com.dosol.qwer.domain.BaseEntity;
import com.dosol.qwer.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardNum;

    // 수정된 부분: CascadeType.PERSIST 추가
    @ManyToOne(cascade = CascadeType.PERSIST)
    private User user;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @ColumnDefault("0")
    private int visitcount;

    public void updateVisitCount() {
        this.visitcount++;
    }

    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }


}
