package com.taomish.msservice01.repo;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Data
@Table(name="xceler_actualizationservice_actualizedquantity")
public class ActualizedQuantityObligations extends ActualizationEntity implements Serializable {

    @Column
    private String actualizedQuantityId;

    @Column
    private TradeTransactionType plannedObligationType;

    @Column
    private Double plannedQuantity;

    @Column
    private Double loadQuantity;

    @Column
    private Double unloadQuantity;

    @Column
    private String quantityUom;

    @Column
    private String brand;

    @Column
    private String grade;

    @Column
    private String origin;

    @Column
    private String commodity;

    @Column
    private String packageType;

    @Column
    private String internalPackage;

    @Column
    private String externalPackage;

    @Column
    private Double internalPackageUnit;

    @Column
    private Double externalPackageUnit;

    @Column
    private Double plannedInternalPackageUnit;

    @Column
    private Double plannedExternalPackageUnit;

    @Column
    private String purpose;

    @Column
    private Integer splitSequenceNumber = 0;

    @Column
    private Double claimedQuantity = 0.0;

    @Column
    private Double adjusted = 0.0;

    @Column
    private Double lossGainQuantity = 0.0;

    @Column
    private LocalDateTime dischargeDate;
    @Column
    private Double balanceAllocateQuantity = 0.0;
    private Double receivedQuantity=0.0;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name="eventCode")
    @Column(name="eventDate")
    @CollectionTable(name="xceler_actualizationservice_actualizedeventdetails", joinColumns=@JoinColumn(name="uuid"))
    Map<String, LocalDateTime> actualizationEventMapping= new HashMap<String, LocalDateTime>();

    public Map<String, LocalDateTime> getActualizationEventMapping() {
        return actualizationEventMapping;
    }

    public Double getActualizedQuantity(TradeTransactionType tradeTransactionType) {
        if(tradeTransactionType.equals(TradeTransactionType.BUY)) {
            return loadQuantity;
        } else {
            return unloadQuantity;
        }
    }
}
