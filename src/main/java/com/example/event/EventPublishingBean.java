package com.example.event;

import com.example.event.extension.AnnotatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublishingBean {
    private final ApplicationEventPublisher eventPublisher;


    public void fireSimpleEvent() {
        eventPublisher.publishEvent(new MySimpleEvent("event message"));
    }

    public void fireChildEvent() {
        eventPublisher.publishEvent(new ChildEvent("event message"));
    }

    public void fireCustomEvent(AnnotatedEvent event) {
        eventPublisher.publishEvent(event);
    }
}
