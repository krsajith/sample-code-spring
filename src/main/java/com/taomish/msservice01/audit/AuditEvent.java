package com.taomish.msservice01.audit;

import org.springframework.context.ApplicationEvent;

public class AuditEvent extends ApplicationEvent {
    public AuditEvent(Object source) {
        super(source);
    }
}
