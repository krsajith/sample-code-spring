package com.taomish.msservice01.datetime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TimeZoneColumn;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "date_sample")
public class DateSample {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "simple_date")
    private LocalDate simpleDate;

    @Column(name = "local_date_time")
    private LocalDateTime locDateTime;

    @Column(name = "col_time_with_zone",columnDefinition = "timestamp with time zone")
    @TimeZoneStorage(TimeZoneStorageType.COLUMN)
    @TimeZoneColumn(name = "col_time_with_zone_tz")
    private OffsetDateTime colTimeWithZone;

    @Column(name = "col_time_with_out_zone")
    private Instant colTimeWithOutZone;

}
