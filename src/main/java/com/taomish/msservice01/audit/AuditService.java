package com.taomish.msservice01.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Maps;
import com.taomish.msservice01.audit.domain.AuditMessage;
import com.taomish.msservice01.audit.domain.Difference;
import com.taomish.msservice01.audit.model.AuditLog;
import com.taomish.msservice01.audit.repo.AuditLogRepo;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@Slf4j
public class AuditService {
    private final AuditLogRepo auditLogRepo;
    private final ResourceLoader resourceLoader;
    private final EntityManager entityManager;

    private final Map<String,String> parentKeyMap = new HashMap<>();

    private List<String> ignoreFields = List.of("uuid","tenantId","createdBy","updatedBy","updatedTimestamp","createdTimestamp");

    @Value("${dateFormat:dd/MM/yyyy, h:m a}")
    private String dateFormat;

    public AuditService(AuditLogRepo auditLogRepo, ResourceLoader resourceLoader, EntityManager entityManager) throws JsonProcessingException {
        this.auditLogRepo = auditLogRepo;
        this.resourceLoader = resourceLoader;
        this.entityManager = entityManager;
    }



    public static String asString(Resource resource) {
        try (Reader reader =
                     new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Resource loadEmployeesWithResourceLoader() {
        return resourceLoader.getResource(
                "classpath:config/entity_hierarchy_definition.json");
    }



    public String getParentId(AuditMessage audit) {
        if(parentKeyMap.containsKey(audit.getEntity())){
            return  audit.getPayload().get(parentKeyMap.get(audit.getEntity())).toString();
        }
        return null;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void saveAudit(AuditMessage audit) {
        try {
            log.info("Received message {}", audit);
            var auditLog = new AuditLog();
            auditLog.setEntityName(audit.getEntity());
            auditLog.setTableName(audit.getTable());
            auditLog.setPayload(audit.getPayload());
            auditLog.setSummary(getSummary(auditLog.getPayload())); //summary is the subset of payload (i.e.,) few abstract base entity fields are removed
            auditLog.setEntityId(getEntityId(audit));
//            auditLog.setUpdateLocation(audit.getUpdateLocation());
            auditLog.setParentId(getParentId(audit));

            var prevLog = auditLogRepo.findFirstByEntityNameAndEntityIdAndLatest(auditLog.getEntityName(), auditLog.getEntityId(), true);
            if (prevLog != null) {
                prevLog.setLatest(false);
                auditLog.setAction("Update");
                var difference = Maps.difference(FlatMapUtil.flatten(prevLog.getSummary()), FlatMapUtil.flatten(auditLog.getSummary()));

                auditLog.setAppended(difference.entriesOnlyOnRight().toString());
                auditLog.setRemoved(difference.entriesOnlyOnLeft().toString());
                auditLog.setModified(difference.entriesDiffering().toString());
                auditLog.setDifferenceList(new ArrayList<>());
                difference.entriesDiffering().forEach((k, v) -> {
                    if (!ObjectUtils.isEmpty(v.leftValue()) || !ObjectUtils.isEmpty(v.rightValue())) {
                        auditLog.getDifferenceList()
                                .add(new Difference(k, StringUtils.capitalize(k.replace("/", " ").trim()), v.leftValue(), v.rightValue()));
                    }
                });

                if (ObjectUtils.isEmpty(auditLog.getDifferenceList())) {
                    return;
                }
                auditLogRepo.save(prevLog);
            }

            auditLogRepo.save(auditLog);
        } catch (Exception e) {
            log.info("Error auditing {}", audit, e);
        }
    }

    private String getEntityId(AuditMessage audit) {
        try {
            return String.valueOf(audit.getPayload().get("uuid"));
        } catch (Exception e) {
            return String.valueOf(audit.getPayload().get("id"));
        }
    }

    private Map<String, Object> getSummary(Map<String, Object> payload) {
        log.info("Entered into AuditListener.getSummary");
        try {
            Map<String, Object> summary = new HashMap<>(payload);
            ignoreFields.forEach(summary.keySet()::remove);
            log.info("Exited from AuditListener.getSummary");
            return summary;
        } catch (Exception e) {
            log.error("Couldn't remove fields from payload, error is :", e);
            return Collections.emptyMap();
        }
    }

//    private Map<String, Object> formatDateFields(Map<String, Object> summary) {
//        log.info("Entered into AuditListener.formatDateFields");
//        for (Map.Entry<String, Object> entry : summary.entrySet()) {
//            try {
//                entry.setValue(getDateFormat(entry.getValue().toString()));
//            } catch (Exception e) {
//                log.error("Error formatting date");
//            }
//        }
//        log.info("Exited from AuditListener.formatDateFields");
//        return summary;
//    }


    private String getDateFormat(String val) {
        log.info("Entered into AuditListener.getDateFormat");
        LocalDateTime dateTime = LocalDateTime.parse(val);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(this.dateFormat);
        log.info("Exited from AuditListener.getDateFormat");
        return dateTime.format(formatter).toUpperCase().replace(".", "");

    }

}
