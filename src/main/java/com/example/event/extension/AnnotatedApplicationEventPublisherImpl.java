package com.example.event.extension;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
@RequiredArgsConstructor
public class AnnotatedApplicationEventPublisherImpl implements AnnotatedApplicationEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publishEvent(Object event) {
        eventPublisher.publishEvent(event);
    }

    @Override
    public void publishEvent(Object event, Annotation... qualifiers) {
        eventPublisher.publishEvent(new AnnotatedEvent(eventPublisher, event, qualifiers));
    }
}
