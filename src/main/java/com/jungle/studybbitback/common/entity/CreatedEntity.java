package com.jungle.studybbitback.common.entity;

import jakarta.persistence.Column;
import org.springframework.data.annotation.CreatedBy;

public class CreatedEntity extends CreatedTimeEntity{
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;
}
