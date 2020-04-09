package com.example;

import com.example.event.ChildEvent;
import com.example.event.MySimpleEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@Component
public class ChildComponent extends AbstractComponent {
    private final SimpleBean bean;
    private final ApplicationEventPublisher eventPublisher;

    public ChildComponent(SimpleBean bean, ApplicationEventPublisher eventPublisher) {
        this.bean = bean;
        this.eventPublisher = eventPublisher;
    }

    public void fireSimpleEvent() {
        eventPublisher.publishEvent(new MySimpleEvent("event message"));
    }

    public void fireChildEvent() {
        eventPublisher.publishEvent(new ChildEvent("event message"));
    }
}
