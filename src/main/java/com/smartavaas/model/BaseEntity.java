package com.smartavaas.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    //@Column(nullable = false)
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean active = true;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(updatable = false)
    private LocalTime createdAt;

    @LastModifiedBy
    private String modifyBy;

    @LastModifiedDate
    private LocalDateTime modifyAt;
}
