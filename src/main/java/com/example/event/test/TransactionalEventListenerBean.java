package com.example.event.test;

import com.example.event.extension.AnnotatedEvent;
import lombok.Getter;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;

@Getter
class TransactionalEventListenerBean {
    private List<MessageEvent> adminEvents = new ArrayList<>();
    private List<MessageEvent> managerEvents = new ArrayList<>();
    private List<MessageEvent> adminDevTopicEvents = new ArrayList<>();
    private List<MessageEvent> managerMainTopicEvents = new ArrayList<>();
    private List<MessageEvent> mainTopicEvents = new ArrayList<>();
    private List<MessageEvent> devTopicEvents = new ArrayList<>();
    private List<MessageEvent> allMessageEvents = new ArrayList<>();
    private List<AnnotatedEvent> allAnnotatedEvents = new ArrayList<>();


   /* @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void handleAdminEvent(@Actor("admin") MessageEvent event) {
        adminEvents.add(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void handleManagerEvent(@Actor("manager") MessageEvent event) {
        managerEvents.add(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void handleAdminDevTopicEvent(@Actor("admin") @Topic("dev") MessageEvent event) {
        adminDevTopicEvents.add(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void handleManagerMainTopicEvent(@Actor("manager") @Topic("main") MessageEvent event) {
        managerMainTopicEvents.add(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void handleMainTopicEvent(@Topic("main") MessageEvent event) {
        mainTopicEvents.add(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void handleDevTopicEvent(@Topic("dev") MessageEvent event) {
        devTopicEvents.add(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void handleAnyMessageEvent(MessageEvent event) {
        allMessageEvents.add(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void handleAnyAnnotatedEvent(AnnotatedEvent event) {
        allAnnotatedEvents.add(event);
    }*/
}
