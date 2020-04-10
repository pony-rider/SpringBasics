package com.example.event.extension;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

import java.lang.annotation.Annotation;

@RequiredArgsConstructor
public class AnnotatedApplicationEventPublisher implements ApplicationEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publishEvent(Object event) {
        eventPublisher.publishEvent(event);
    }

    public void publishEvent(Object event, Annotation... qualifiers) {
        eventPublisher.publishEvent(new AnnotatedEvent(eventPublisher, event, qualifiers));
    }
}
