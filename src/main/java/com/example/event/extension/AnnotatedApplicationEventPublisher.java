package com.example.event.extension;

import org.springframework.context.ApplicationEventPublisher;

import java.lang.annotation.Annotation;

public interface AnnotatedApplicationEventPublisher extends ApplicationEventPublisher {

    void publishEvent(Object event, Annotation... qualifiers);

}
