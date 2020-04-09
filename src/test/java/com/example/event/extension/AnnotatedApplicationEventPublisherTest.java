package com.example.event.extension;


import lombok.Data;
import lombok.Getter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.EventListenerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AnnotatedApplicationEventPublisherTest.TestConfig.class)
public class AnnotatedApplicationEventPublisherTest {
    @Autowired
    private AnnotatedApplicationEventPublisher eventPublisher;

    @Autowired
    private EventListenerBean eventListenerBean;

    @Configuration
    public static class TestConfig {

        @Bean
        public AnnotatedApplicationEventPublisher getPublisher(ApplicationEventPublisher applicationEventPublisher) {
            return new AnnotatedApplicationEventPublisherImpl(applicationEventPublisher);
        }

        @Bean
        public EventListenerBean getEventListenerBean() {
            return new EventListenerBean();
        }

        @Bean
        public EventListenerFactory getEventListenerFactory() {
            return new AnnotatedEventListenerFactory();
        }
    }


    @Test
    public void eventNotificationTest() {
        assertNotNull(eventListenerBean);
        assertNotNull(eventPublisher);
        Actor admin = AnnotationUtils.createAnnotation(Actor.class, "admin");
        Actor manager = AnnotationUtils.createAnnotation(Actor.class, "manager");
        Topic main = AnnotationUtils.createAnnotation(Topic.class, "main");
        Topic dev = AnnotationUtils.createAnnotation(Topic.class, "dev");
        List<MessageEvent> events = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            events.add(new MessageEvent(i));
        }

        eventPublisher.publishEvent(events.get(0), admin);
        eventPublisher.publishEvent(events.get(1), manager);
        eventPublisher.publishEvent(events.get(2), manager, main);
        eventPublisher.publishEvent(events.get(3), manager, dev);
        eventPublisher.publishEvent(events.get(4), admin, main);
        eventPublisher.publishEvent(events.get(5), admin, dev);
        eventPublisher.publishEvent(events.get(6), dev);
        eventPublisher.publishEvent(events.get(7), main);
        eventPublisher.publishEvent(events.get(8));
        eventPublisher.publishEvent("string event");
        eventPublisher.publishEvent(new Object());

        //compare events number
        assertEquals(3 , eventListenerBean.getAdminEvents().size());
        assertEquals(3 , eventListenerBean.getManagerEvents().size());
        assertEquals(3 , eventListenerBean.getDevTopicEvents().size());
        assertEquals(3 , eventListenerBean.getMainTopicEvents().size());
        assertEquals(1 , eventListenerBean.getManagerMainTopicEvents().size());
        assertEquals(1 , eventListenerBean.getAdminDevTopicEvents().size());
        assertEquals(9 , eventListenerBean.getAllMessageEvents().size());

        //compare events content
        assertEquals(Arrays.asList(events.get(0), events.get(4), events.get(5)) , eventListenerBean.getAdminEvents());
        assertEquals(Arrays.asList(events.get(1), events.get(2), events.get(3)) , eventListenerBean.getManagerEvents());
        assertEquals(Arrays.asList(events.get(3), events.get(5), events.get(6)) , eventListenerBean.getDevTopicEvents());
        assertEquals(Arrays.asList(events.get(2), events.get(4), events.get(7)) , eventListenerBean.getMainTopicEvents());
        assertEquals(Arrays.asList(events.get(5)) , eventListenerBean.getAdminDevTopicEvents());
        assertEquals(Arrays.asList(events.get(2)) , eventListenerBean.getManagerMainTopicEvents());
        assertEquals(events, eventListenerBean.getAllMessageEvents());
    }


    @Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Actor {
        String value();
    }

    @Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Topic {
        String value();
    }

    @Data
    public static class MessageEvent {
        private final int id;
    }


    @Getter
    public static class EventListenerBean {
        private List<MessageEvent> adminEvents = new ArrayList<>();
        private List<MessageEvent> managerEvents = new ArrayList<>();
        private List<MessageEvent> adminDevTopicEvents = new ArrayList<>();
        private List<MessageEvent> managerMainTopicEvents = new ArrayList<>();
        private List<MessageEvent> mainTopicEvents = new ArrayList<>();
        private List<MessageEvent> devTopicEvents = new ArrayList<>();
        private List<MessageEvent> allMessageEvents = new ArrayList<>();


        @EventListener
        public void handleAdminEvent(@Actor("admin") MessageEvent event) {
            System.out.println("receive admin event: " + event);
            adminEvents.add(event);
        }

        @EventListener
        public void handleManagerEvent(@Actor("manager") MessageEvent event) {
            System.out.println("receive manager event: " + event);
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
        public void handleAllEvent(MessageEvent event) {
            System.out.println("receive unqualified event: " + event);
            allMessageEvents.add(event);
        }

    }


}