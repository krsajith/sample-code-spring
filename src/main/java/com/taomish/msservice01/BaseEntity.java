package com.taomish.msservice01;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(updatable = false)
    private String createdBy;

    private String updatedBy;

    @Column(updatable = false)
    private LocalDateTime createdTimestamp;

    private LocalDateTime updatedTimestamp;

    private String tenantId;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID uuid;
}
