package kr.ac.kopo.polycommunity.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {


    @CreationTimestamp
    @Column(updatable = false) // 생성 시에만 값 설정
    private LocalDateTime createdTime;


    @UpdateTimestamp
    @Column(insertable = false) // 수정 시에만 값 갱신
    private LocalDateTime updatedTime;
}
