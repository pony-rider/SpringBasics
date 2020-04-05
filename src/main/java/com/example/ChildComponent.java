package com.example;

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

    public void doSomething() {
        eventPublisher.publishEvent(new MySimpleEvent("event message"));
    }
}
