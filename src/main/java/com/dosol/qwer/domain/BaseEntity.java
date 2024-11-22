package com.dosol.qwer.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // JPA Auditing 활성화
public abstract class BaseEntity {

    @CreatedDate // 생성 시간 자동 저장
    @Column(nullable = false, updatable = false) // 생성 후 수정 불가
    private LocalDateTime createTime;

    @LastModifiedDate // 수정 시간 자동 업데이트
    @Column(nullable = false)
    private LocalDateTime updateTime;
}
