package com.taomish.msservice01;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "customer")
@ToString
public class Customer extends BaseEntity {


    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "plans")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> plans;

    @Column(name = "tenant_id")
    private String tenantId;

    private boolean skip;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Set<CustomerAddress> customerAddresses;
}
