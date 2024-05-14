package com.taomish.msservice01.audit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taomish.msservice01.BaseEntity;
import com.taomish.msservice01.audit.domain.AuditMessage;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class AuditListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {

    private final Set<String> excludedEntities = Set.of("AuditLog", "HttpTraceAuditLog");

    private final AuditService auditService;

    private final EntityManager entityManager;

    private final Logger log = LoggerFactory.getLogger(AuditListener.class);

    TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
    };

    private final ObjectMapper objectMapper = JsonUtils.getObjectMapper();

    public AuditListener(AuditService auditService, EntityManager entityManager) {
        this.auditService = auditService;
        this.entityManager = entityManager;
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        saveAudit(event.getEntity(),false);
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        saveAudit(event.getEntity(),false);
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister persister) {
        return false;
    }

    private void saveAudit(Object baseEntity,boolean isDelete) {
        try {
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
                    tableName, objectMapper.convertValue(baseEntity, typeRef), "",isDelete));
            log.debug("Updating {}", baseEntity);

        } catch (Throwable e) {
            log.debug("Error auditing ", e);
        }
    }

    @Override
    public void onPostDelete(PostDeleteEvent event) {
        entityManager.detach(event.getEntity());
        saveAudit(event.getEntity(),true);
    }
}
