package com.taomish.msservice01.audit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taomish.msservice01.BaseEntity;
import com.taomish.msservice01.audit.domain.AuditMessage;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;
import java.util.Set;

@Component
public class EntityChangeListener  {

    private final Set<String> excludedEntities = Set.of("AuditLog", "HttpTraceAuditLog");

    private final Logger log = LoggerFactory.getLogger(EntityChangeListener.class);

    private final AuditService auditService;

    TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
    };

    private final ObjectMapper objectMapper = JsonUtils.getObjectMapper();

    public EntityChangeListener(AuditService auditService) {
        this.auditService = auditService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onApplicationEvent(AuditEvent event) {
        log.info("onApplicationEvent: {}", event);
        try {
            var baseEntity = event.getSource();
            String simpleName = baseEntity.getClass().getSimpleName();
            if (excludedEntities.contains(simpleName)) return;
            String tableName;
            var table = baseEntity.getClass().getAnnotation(Table.class);
            if (table != null) {
                tableName = table.name();
            } else {
                var entity = baseEntity.getClass().getAnnotation(Entity.class);
                tableName = entity.name();
            }
            auditService.saveAudit(new AuditMessage(simpleName, ((BaseEntity) baseEntity).getUuid().toString(),
                    tableName, objectMapper.convertValue(baseEntity, typeRef), ""));
            log.debug("Updating {}", baseEntity);
        } catch (Throwable e) {
            log.debug("Error setting user details", e);
        }
    }
}
