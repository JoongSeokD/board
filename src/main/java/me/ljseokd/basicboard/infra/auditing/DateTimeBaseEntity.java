package me.ljseokd.basicboard.infra.auditing;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class DateTimeBaseEntity {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDateTime;
    @LastModifiedDate
    private LocalDateTime lastModifiedTime;

    @PrePersist
    private void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createDateTime = now;
        lastModifiedTime = now;
    }

    @PreUpdate
    public void preUpdate(){
        lastModifiedTime = LocalDateTime.now();
    }
}
