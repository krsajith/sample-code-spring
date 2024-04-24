package com.taomish.msservice01.audit.domain;

import lombok.Data;

@Data
public class EntityRelation {
    private String relatedEntityName;
    private String linkAttributeInRelatedEntity;
    private String linkAttributeInMainEntity;
}
