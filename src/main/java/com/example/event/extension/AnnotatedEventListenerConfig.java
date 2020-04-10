package com.example.event.extension;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListenerFactory;
import org.springframework.transaction.event.TransactionalEventListenerFactory;

@Configuration
public class AnnotatedEventListenerConfig {
    @Bean
    public AnnotatedApplicationEventPublisher getAnnotatedPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new AnnotatedApplicationEventPublisher(applicationEventPublisher);
    }

    @Bean
    public EventListenerFactory getEventListenerFactory() {
        return new AnnotatedEventListenerFactory();
    }

    @Bean
    public TransactionalEventListenerFactory getTransactionalEventListenerFactory() {
        return new AnnotatedTransactionalEventListenerFactory();
    }
}
