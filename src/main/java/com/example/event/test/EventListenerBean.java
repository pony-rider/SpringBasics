package com.example.event.test;

import com.example.event.extension.AnnotatedEvent;
import lombok.Getter;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
class EventListenerBean {
    private List<MessageEvent> adminEvents = new ArrayList<>();
    private List<MessageEvent> managerEvents = new ArrayList<>();
    private List<MessageEvent> adminDevTopicEvents = new ArrayList<>();
    private List<MessageEvent> managerMainTopicEvents = new ArrayList<>();
    private List<MessageEvent> mainTopicEvents = new ArrayList<>();
    private List<MessageEvent> devTopicEvents = new ArrayList<>();
    private List<MessageEvent> allMessageEvents = new ArrayList<>();
    private List<AnnotatedEvent> allAnnotatedEvents = new ArrayList<>();


    @EventListener
    public void handleAdminEvent(@Actor("admin") MessageEvent event) {
        adminEvents.add(event);
    }

    @EventListener
    public void handleManagerEvent(@Actor("manager") MessageEvent event) {
        managerEvents.add(event);
    }

    @EventListener
    public void handleAdminDevTopicEvent(@Actor("admin") @Topic("dev") MessageEvent event) {
        adminDevTopicEvents.add(event);
    }

    @EventListener
    public void handleManagerMainTopicEvent(@Actor("manager") @Topic("main") MessageEvent event) {
        managerMainTopicEvents.add(event);
    }

    @EventListener
    public void handleMainTopicEvent(@Topic("main") MessageEvent event) {
        mainTopicEvents.add(event);
    }

    @EventListener
    public void handleDevTopicEvent(@Topic("dev") MessageEvent event) {
        devTopicEvents.add(event);
    }

    @EventListener
    public void handleAnyMessageEvent(MessageEvent event) {
        allMessageEvents.add(event);
    }

    @EventListener
    public void handleAnyAnnotatedEvent(AnnotatedEvent event) {
        allAnnotatedEvents.add(event);
    }
}
