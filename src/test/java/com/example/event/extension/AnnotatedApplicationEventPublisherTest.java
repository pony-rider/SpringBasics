package com.example.event.extension;


import com.example.repository.CountryRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
//@SpringBootTest
@SpringBootTest(classes= AnnotatedApplicationEventPublisherTest.TestConfig.class)
@Import(AnnotatedEventListenerConfig.class)
public class AnnotatedApplicationEventPublisherTest {
    static Actor admin = AnnotationUtils.createAnnotation(Actor.class, "admin");
    static Actor manager = AnnotationUtils.createAnnotation(Actor.class, "manager");
    static Topic main = AnnotationUtils.createAnnotation(Topic.class, "main");
    static Topic dev = AnnotationUtils.createAnnotation(Topic.class, "dev");

    @Autowired
    private EventListenerBean eventListenerBean;

    @Autowired
    private TransactionalEventListenerBean transactionalEventListenerBean;

    @Autowired
    private EventPublisherBean eventPublisherBean;

    @Configuration
    public static class TestConfig {

        @Bean
        public EventListenerBean getEventListenerBean() {
            return new EventListenerBean();
        }

        @Bean
        public TransactionalEventListenerBean getTransactionalEventListenerBean() {
            return new TransactionalEventListenerBean();
        }

        @Bean
        public EventPublisherBean eventPublisherBean(AnnotatedApplicationEventPublisher publisher) {
            return new EventPublisherBean(publisher);
        }

        //create datasource for test purpose
        @Bean
        public PlatformTransactionManager transactionManager() {
            DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
            dataSourceBuilder.driverClassName("org.hsqldb.jdbc.JDBCDriver");
            dataSourceBuilder.url("jdbc:hsqldb:mem:testdb;DB_CLOSE_DELAY=-1");
            dataSourceBuilder.username("SA");
            dataSourceBuilder.password("");
            return new DataSourceTransactionManager(dataSourceBuilder.build());
        }
    }

    @Test
    public void testAnnotatedEventListenerBean() {
        assertNotNull(eventListenerBean);
        List<MessageEvent> events = eventPublisherBean.createMessageEvents(9);
        eventPublisherBean.publishEvents(events);

        //compare events number
        assertEquals(3, eventListenerBean.getAdminEvents().size());
        assertEquals(3, eventListenerBean.getManagerEvents().size());
        assertEquals(3, eventListenerBean.getDevTopicEvents().size());
        assertEquals(3, eventListenerBean.getMainTopicEvents().size());
        assertEquals(1, eventListenerBean.getManagerMainTopicEvents().size());
        assertEquals(1, eventListenerBean.getAdminDevTopicEvents().size());
        assertEquals(9, eventListenerBean.getAllMessageEvents().size());
        assertEquals(9, eventListenerBean.getAllAnnotatedEvents().size());

        //compare events content
        assertEquals(Arrays.asList(events.get(0), events.get(4), events.get(5)), eventListenerBean.getAdminEvents());
        assertEquals(Arrays.asList(events.get(1), events.get(2), events.get(3)), eventListenerBean.getManagerEvents());
        assertEquals(Arrays.asList(events.get(3), events.get(5), events.get(6)), eventListenerBean.getDevTopicEvents());
        assertEquals(Arrays.asList(events.get(2), events.get(4), events.get(7)), eventListenerBean.getMainTopicEvents());
        assertEquals(Arrays.asList(events.get(5)), eventListenerBean.getAdminDevTopicEvents());
        assertEquals(Arrays.asList(events.get(2)), eventListenerBean.getManagerMainTopicEvents());
        assertEquals(events, eventListenerBean.getAllMessageEvents());
    }

    @Test
    @Transactional
    public void testTransactionalAnnotatedEventListenerBean() {
        assertNotNull(transactionalEventListenerBean);
        List<MessageEvent> events = eventPublisherBean.createMessageEvents(9);
        eventPublisherBean.publishEvents(events);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        //compare events number
        assertEquals(3, transactionalEventListenerBean.getAdminEvents().size());
        assertEquals(3, transactionalEventListenerBean.getManagerEvents().size());
        assertEquals(3, transactionalEventListenerBean.getDevTopicEvents().size());
        assertEquals(3, transactionalEventListenerBean.getMainTopicEvents().size());
        assertEquals(1, transactionalEventListenerBean.getManagerMainTopicEvents().size());
        assertEquals(1, transactionalEventListenerBean.getAdminDevTopicEvents().size());
        assertEquals(9, transactionalEventListenerBean.getAllMessageEvents().size());
        assertEquals(9, transactionalEventListenerBean.getAllAnnotatedEvents().size());

        //compare events content
        assertEquals(Arrays.asList(events.get(0), events.get(4), events.get(5)), transactionalEventListenerBean.getAdminEvents());
        assertEquals(Arrays.asList(events.get(1), events.get(2), events.get(3)), transactionalEventListenerBean.getManagerEvents());
        assertEquals(Arrays.asList(events.get(3), events.get(5), events.get(6)), transactionalEventListenerBean.getDevTopicEvents());
        assertEquals(Arrays.asList(events.get(2), events.get(4), events.get(7)), transactionalEventListenerBean.getMainTopicEvents());
        assertEquals(Arrays.asList(events.get(5)), transactionalEventListenerBean.getAdminDevTopicEvents());
        assertEquals(Arrays.asList(events.get(2)), transactionalEventListenerBean.getManagerMainTopicEvents());
        assertEquals(events, transactionalEventListenerBean.getAllMessageEvents());
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

    @Getter
    public static class TransactionalEventListenerBean {
        private List<MessageEvent> adminEvents = new ArrayList<>();
        private List<MessageEvent> managerEvents = new ArrayList<>();
        private List<MessageEvent> adminDevTopicEvents = new ArrayList<>();
        private List<MessageEvent> managerMainTopicEvents = new ArrayList<>();
        private List<MessageEvent> mainTopicEvents = new ArrayList<>();
        private List<MessageEvent> devTopicEvents = new ArrayList<>();
        private List<MessageEvent> allMessageEvents = new ArrayList<>();
        private List<AnnotatedEvent> allAnnotatedEvents = new ArrayList<>();


        @TransactionalEventListener
        public void handleAdminEvent(@Actor("admin") MessageEvent event) {
            adminEvents.add(event);
        }

        @TransactionalEventListener
        public void handleManagerEvent(@Actor("manager") MessageEvent event) {
            managerEvents.add(event);
        }

        @TransactionalEventListener
        public void handleAdminDevTopicEvent(@Actor("admin") @Topic("dev") MessageEvent event) {
            adminDevTopicEvents.add(event);
        }

        @TransactionalEventListener
        public void handleManagerMainTopicEvent(@Actor("manager") @Topic("main") MessageEvent event) {
            managerMainTopicEvents.add(event);
        }

        @TransactionalEventListener
        public void handleMainTopicEvent(@Topic("main") MessageEvent event) {
            mainTopicEvents.add(event);
        }

        @TransactionalEventListener
        public void handleDevTopicEvent(@Topic("dev") MessageEvent event) {
            devTopicEvents.add(event);
        }

        @TransactionalEventListener
        public void handleAnyMessageEvent(MessageEvent event) {
            allMessageEvents.add(event);
        }

        @TransactionalEventListener
        public void handleAnyAnnotatedEvent(AnnotatedEvent event) {
            allAnnotatedEvents.add(event);
        }
    }

    @RequiredArgsConstructor
    public static class EventPublisherBean {

        private final AnnotatedApplicationEventPublisher eventPublisher;

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

        public List<MessageEvent> createMessageEvents(int count) {
            List<MessageEvent> events = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                events.add(new MessageEvent(i));
            }
            return events;
        }

    }
}
