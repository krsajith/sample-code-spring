package com.taomish.msservice01.audit.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taomish.msservice01.audit.domain.Difference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "ctrm_audit_log")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String entityName;
    private String entityId;
    private String tableName;
    private Boolean latest = true;
    private String action = "Create";
    private String parentId;

    @JdbcTypeCode( SqlTypes.JSON )
    @Column(columnDefinition = "jsonb")
    private Map<String,Object> payload;

    @JdbcTypeCode( SqlTypes.JSON )
    @Column(columnDefinition = "jsonb")
    private Map<String,Object> summary;

    @JdbcTypeCode( SqlTypes.JSON )
    @Column(columnDefinition = "jsonb")
    private List<Difference> differenceList;


    @Column(columnDefinition = "text")
    private String appended;

    @Column(columnDefinition = "text")
    private String removed;

    @Column(columnDefinition = "text")
    private String modified;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")
    private LocalDateTime createdTimestamp;
//    private String  updateLocation;
}
