package com.example.event.extension;


import com.example.entity.Country;
import com.example.repository.CountryRepository;
import lombok.Data;
import lombok.Getter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.EventListenerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TestContextTransactionUtils;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionalEventListenerFactory;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
//@EnableAutoConfiguration
@ContextConfiguration(classes = AnnotatedApplicationEventPublisherTest.TestConfig.class)
//@TestConfiguration("AnnotatedApplicationEventPublisherTest.TestConfig.class")
//@Import(AnnotatedApplicationEventPublisherTest.TestConfig.class)
//@DataJpaTest
public class AnnotatedApplicationEventPublisherTest {
    Actor admin = AnnotationUtils.createAnnotation(Actor.class, "admin");
    Actor manager = AnnotationUtils.createAnnotation(Actor.class, "manager");
    Topic main = AnnotationUtils.createAnnotation(Topic.class, "main");
    Topic dev = AnnotationUtils.createAnnotation(Topic.class, "dev");

    @Autowired
    private AnnotatedApplicationEventPublisher eventPublisher;

    @Autowired
    private EventListenerBean eventListenerBean;

    @Autowired
    private TransactionalEventListenerBean transactionalEventListenerBean;

    @Autowired
    private PlatformTransactionManager transactionManager;

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
        public TransactionalEventListenerBean getTransactionalEventListenerBean() {
            return new TransactionalEventListenerBean();
        }

        @Bean
        public EventListenerFactory getEventListenerFactory() {
            return new AnnotatedEventListenerFactory();
        }

       /* @Bean
        public TransactionalEventListenerFactory getTransactionalEventListenerFactory() {
            return new AnnotatedTransactionalEventListenerFactory();
        }*/


        @Bean
        public PlatformTransactionManager transactionManager() {
            return new PlatformTransactionManager() {
                @Override
                public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
                    return null;
                }

                @Override
                public void commit(TransactionStatus status) throws TransactionException {
                }

                @Override
                public void rollback(TransactionStatus status) throws TransactionException {
                }
            };
        }
    }

    private void publishEvents(List<MessageEvent> events) {
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
    private void publishEventsInTransaction(List<MessageEvent> events) {
        publishEvents(events);
    }

    private List<MessageEvent> createMessageEvents(int count) {
        List<MessageEvent> events = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            events.add(new MessageEvent(i));
        }
        return events;
    }

    @Test
    public void testAnnotatedEventListenerBean() {
        assertNotNull(eventListenerBean);
        assertNotNull(eventPublisher);
        List<MessageEvent> events = createMessageEvents(9);
        publishEvents(events);

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

    public void testTransactionalAnnotatedEventListenerBean() {
        assertNotNull(transactionalEventListenerBean);
        assertNotNull(eventPublisher);

        List<MessageEvent> events = createMessageEvents(9);

        publishEventsInTransaction(events);

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


        @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
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
        }
    }

}
