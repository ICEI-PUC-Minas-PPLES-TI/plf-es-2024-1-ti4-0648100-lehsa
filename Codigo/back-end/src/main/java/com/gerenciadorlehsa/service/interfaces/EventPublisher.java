package com.gerenciadorlehsa.service.interfaces;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

public interface EventPublisher extends ApplicationEventPublisherAware {

    default void publishEvent(ApplicationEvent event) {
        ApplicationEventPublisher eventPublisher = getEventPublisher();
        if (eventPublisher != null) {
            eventPublisher.publishEvent(event);
        }
    }
    ApplicationEventPublisher getEventPublisher();
}