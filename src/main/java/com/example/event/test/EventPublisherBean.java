package com.example.event.test;

import com.example.event.extension.AnnotatedApplicationEventPublisher;
import com.example.event.extension.AnnotationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

//@Component
@RequiredArgsConstructor
public class EventPublisherBean {
    static Actor admin = AnnotationUtils.createAnnotation(Actor.class, "admin");
    static Actor manager = AnnotationUtils.createAnnotation(Actor.class, "manager");
    static Topic main = AnnotationUtils.createAnnotation(Topic.class, "main");
    static Topic dev = AnnotationUtils.createAnnotation(Topic.class, "dev");

    @Autowired
    private AnnotatedApplicationEventPublisher eventPublisher;

    public void publishEvents(List<MessageEvent> events) {
        eventPublisher.publishEvent(events.get(0), admin);
        eventPublisher.publishEvent(events.get(1), manager);
        eventPublisher.publishEvent(events.get(2), manager, main);
        eventPublisher.publishEvent(events.get(3), manager, dev);
        eventPublisher.publishEvent(events.get(4), admin, main);
        eventPublisher.publishEvent(events.get(5), admin, dev);
        eventPublisher.publishEvent(events.get(6), dev);
        eventPublisher.publishEvent(events.get(7), main);
        eventPublisher.publishEvent(events.get(8));
        eventPublisher.publishEvent("string event", main);
    }

    @Transactional
    public void publishEventsInTransaction(List<MessageEvent> events) {
        publishEvents(events);
    }

    public List<MessageEvent> createMessageEvents(int count) {
        List<MessageEvent> events = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            events.add(new MessageEvent(i));
        }
        return events;
    }

}
