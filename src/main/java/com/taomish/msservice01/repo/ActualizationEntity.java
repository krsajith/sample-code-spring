package com.taomish.msservice01.repo;



import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ActualizationEntity extends AbstractBaseEntity {

    private String actualizationId;

    private String plannedObligationId;

    private LocalDateTime actualizedOn = LocalDateTime.now();

    private String tradeId;

    public String getActualizationId() {
        return actualizationId;
    }

    public void setActualizationId(String actualizationId) {
        this.actualizationId = actualizationId;
    }

    public String getPlannedObligationId() {
        return plannedObligationId;
    }

    public void setPlannedObligationId(String plannedObligationId) {
        this.plannedObligationId = plannedObligationId;
    }

    public LocalDateTime getActualizedOn() {
        return actualizedOn;
    }

    public void setActualizedOn(LocalDateTime actualizedOn) {
        this.actualizedOn = actualizedOn;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }
}
