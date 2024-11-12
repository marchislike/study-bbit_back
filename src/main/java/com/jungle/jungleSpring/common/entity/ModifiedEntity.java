package com.jungle.jungleSpring.common.entity;

import jakarta.persistence.Column;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

public class ModifiedEntity extends ModifiedTimeEntity{
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;
    @LastModifiedBy
    private String modifiedBy;
}
