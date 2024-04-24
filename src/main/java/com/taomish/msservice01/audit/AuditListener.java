package com.taomish.msservice01.audit;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuditListener implements PostInsertEventListener, PostUpdateEventListener {

    private final ApplicationEventPublisher publisher;
    private final ModelMapperService modelMapperService;

    public AuditListener(ApplicationEventPublisher publisher, ModelMapperService modelMapperService) {
        this.publisher = publisher;

        this.modelMapperService = modelMapperService;
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        log.info("sendingEvent  {}",event.getEntity().getClass().getSimpleName());
        publisher.publishEvent(new AuditEvent(modelMapperService.map(event.getEntity(),event.getEntity().getClass())));
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        log.info("sendingEvent {}",event.getEntity().getClass().getSimpleName());
        publisher.publishEvent(new AuditEvent(modelMapperService.map(event.getEntity(),event.getEntity().getClass())));
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister persister) {
        return true;
    }






}
