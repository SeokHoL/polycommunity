package kr.ac.kopo.polycommunity.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {

    @CreationTimestamp
    @Column(updatable = false) //업데이트할때는 이놈이 관여안하게끔 설정
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(insertable = false) //추가 및 삽입할때는 이놈이 관여안하게끔 설정
    private LocalDateTime updatedTime;

}
