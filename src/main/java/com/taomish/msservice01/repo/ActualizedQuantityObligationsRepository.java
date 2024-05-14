package com.taomish.msservice01.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActualizedQuantityObligationsRepository extends JpaRepository<ActualizedQuantityObligations, UUID> {
    List<ActualizedQuantityObligations> findAllByTenantIdAndPlannedObligationId(String tenantId, String plannedObligationId);

}
