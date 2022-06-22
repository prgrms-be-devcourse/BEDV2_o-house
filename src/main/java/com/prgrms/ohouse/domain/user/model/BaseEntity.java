package com.prgrms.ohouse.domain.user.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
public class BaseEntity {

    @Column(updatable = false)
    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    @PrePersist
    public void preSet() {
        LocalDateTime now = LocalDateTime.now();
        created_at = now;
        updated_at = now;
    }

    @PreUpdate
    public void update(){
        updated_at = LocalDateTime.now();
    }

}
