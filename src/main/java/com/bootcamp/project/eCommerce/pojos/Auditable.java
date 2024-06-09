package com.bootcamp.project.eCommerce.pojos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class Auditable {

    @NotNull
    @CreatedBy
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    String createdBy;

    @NotNull
    @CreatedDate
    @Column(nullable = false, updatable = false)
    Date createdDate;

    @NotNull
    @LastModifiedDate
    @Column(nullable = false)
    Date lastModifiedDate;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedBy
    String lastModifiedBy;
}
