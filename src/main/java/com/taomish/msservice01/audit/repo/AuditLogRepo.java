
package com.taomish.msservice01.audit.repo;

import com.taomish.msservice01.audit.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepo extends CrudRepository<AuditLog, Long>, JpaRepository<AuditLog, Long> {
    AuditLog findFirstByEntityNameAndEntityIdAndLatest(String entityName, String entityId, Boolean latest);
    List<AuditLog> findAllByEntityNameAndEntityId(String entityName, String entityId);

    List<AuditLog> findAllByEntityName(String entityName);

    @Query(value = "WITH RECURSIVE cte AS ( " +
            "   SELECT id, action, latest, removed, modified, created_timestamp, appended, summary, entity_name, table_name, entity_id, payload, payload->>'createdBy' AS created_by, payload->>'tradeId' AS trade_id, difference_list, parent_id, 1 AS level " +
            "   FROM ctrm_audit_log " +
            "   WHERE " +
            "   (:columnName = 'Entity' AND entity_name ILIKE %:searchValue%) OR " +
            "   (:columnName = 'User' AND payload->>'createdBy' ILIKE %:searchValue%) OR " +
            "   (:columnName = 'Trade' AND payload->>'tradeId' ILIKE %:searchValue%)" +
            "   UNION ALL " +
            "   SELECT t.id, t.action, t.latest, t.removed, t.modified, t.created_timestamp, t.appended, t.summary, t.entity_name, t.table_name, t.entity_id, t.payload, t.payload->>'createdBy' AS created_by, t.payload->>'tradeId' AS trade_id, t.difference_list, t.parent_id, c.level + 1 " +
            "   FROM cte c " +
            "   JOIN ctrm_audit_log t ON t.parent_id = c.entity_id " +
            ") " +
            "SELECT * " +
            "FROM cte " +
            "ORDER BY created_timestamp DESC, CASE WHEN level=1 THEN entity_id ELSE parent_id END, level",
            countQuery = "SELECT count(*) FROM ( " +
                    "   WITH RECURSIVE cte AS ( " +
                    "      SELECT entity_id " +
                    "      FROM ctrm_audit_log " +
                    "      WHERE " +
                    "      (:columnName = 'Entity' AND entity_name ILIKE %:searchValue%) OR " +
                    "      (:columnName = 'User' AND payload->>'createdBy' ILIKE %:searchValue%) OR " +
                    "      (:columnName = 'Trade' AND payload->>'tradeId' ILIKE %:searchValue%)" +
                    "      UNION ALL " +
                    "      SELECT t.entity_id " +
                    "      FROM cte c " +
                    "      JOIN ctrm_audit_log t ON t.parent_id = c.entity_id " +
                    "   ) " +
                    "   SELECT * FROM cte" +
                    ") AS count_query",
            nativeQuery = true)
    Page<AuditLog> findAuditLogsByCriteria(String columnName, String searchValue, Pageable pageable);

    @Query(value = "WITH RECURSIVE cte AS ( " +
            "   SELECT id, action, latest, removed, modified, created_timestamp, appended, summary, entity_name, table_name, entity_id, payload, difference_list, parent_id, 1 AS level " +
            "   FROM ctrm_audit_log " +
            "   UNION ALL " +
            "   SELECT t.id, t.action, t.latest, t.removed, t.modified, t.created_timestamp, t.appended, t.summary, t.entity_name, t.table_name, t.entity_id, t.payload, t.difference_list, t.parent_id, c.level + 1 " +
            "   FROM cte c " +
            "   JOIN ctrm_audit_log t ON t.parent_id = c.entity_id " +
            ") " +
            "SELECT * " +
            "FROM cte " +
            "ORDER BY created_timestamp DESC, CASE WHEN level=1 THEN entity_id ELSE parent_id END, level",
            countQuery = "SELECT count(*) FROM ( " +
                    "   WITH RECURSIVE cte AS ( " +
                    "      SELECT entity_id " +
                    "      FROM ctrm_audit_log " +
                    "      UNION ALL " +
                    "      SELECT t.entity_id " +
                    "      FROM cte c " +
                    "      JOIN ctrm_audit_log t ON t.parent_id = c.entity_id " +
                    "   ) " +
                    "   SELECT * FROM cte" +
                    ") AS count_query",
            nativeQuery = true)
    Page<AuditLog> findAuditLogs(Pageable pageable);

}
