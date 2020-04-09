package com.example.event.extension;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ApplicationListenerMethodAdapter;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Just copied main functionality from org.springframework.transaction.event.ApplicationListenerMethodTransactionalAdapter
 * Can't extend it because it has package-private access
 */
public class AnnotatedTransactionalApplicationEventListener extends ApplicationListenerMethodAdapter {

    private final TransactionalEventListener annotation;
    private List<Annotation> listenerQualifiers;

    public AnnotatedTransactionalApplicationEventListener(String beanName, Class<?> targetClass, Method method) {
        super(beanName, targetClass, method);
        TransactionalEventListener ann = AnnotatedElementUtils.findMergedAnnotation(method, TransactionalEventListener.class);
        if (ann == null) {
            throw new IllegalStateException("No TransactionalEventListener annotation found on method: " + method);
        }
        this.annotation = ann;
        this.listenerQualifiers = method.getParameterCount() > 0 ?
                Arrays.asList(method.getParameterAnnotations()[0]) : Collections.emptyList();
    }

    /*@Override
    public boolean supportsEventType(ResolvableType eventType) {
        System.out.println("supports " + super.supportsEventType(eventType) + " " + eventType.getSource().getClass());
        return super.supportsEventType(eventType);
    }*/

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("transactional event listener enter on application event " + event);
        if (TransactionSynchronizationManager.isSynchronizationActive() &&
                TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronization transactionSynchronization = createTransactionSynchronization(event);
            TransactionSynchronizationManager.registerSynchronization(transactionSynchronization);
        } else if (this.annotation.fallbackExecution()) {
            if (this.annotation.phase() == TransactionPhase.AFTER_ROLLBACK && logger.isWarnEnabled()) {
                logger.warn("Processing " + event + " as a fallback execution on AFTER_ROLLBACK phase");
            }
            processEvent(event);
        } else {
            // No transactional event execution at all
            if (logger.isDebugEnabled()) {
                logger.debug("No transaction is active - skipping " + event);
            }
        }
    }

    @Override
    public void processEvent(ApplicationEvent event) {
        System.out.println("transactional event listener process event " + event);
        if (AnnotatedEvent.class.isAssignableFrom(event.getClass())) {
            AnnotatedEvent annotatedEvent = (AnnotatedEvent) event;
            boolean qualifiersMatches = AnnotationMatcher.qualifiersMatches(listenerQualifiers, annotatedEvent.getAnnotations());
            if (qualifiersMatches) {
                super.processEvent(event);
            }
        } else {
            if (listenerQualifiers.isEmpty()) {
                super.processEvent(event);
            }
        }
    }

    private TransactionSynchronization createTransactionSynchronization(ApplicationEvent event) {
        return new AnnotatedTransactionalApplicationEventListener.TransactionSynchronizationEventAdapter(this, event, this.annotation.phase());
    }


    private static class TransactionSynchronizationEventAdapter extends TransactionSynchronizationAdapter {

        private final ApplicationListenerMethodAdapter listener;

        private final ApplicationEvent event;

        private final TransactionPhase phase;

        public TransactionSynchronizationEventAdapter(ApplicationListenerMethodAdapter listener,
                                                      ApplicationEvent event, TransactionPhase phase) {
            this.listener = listener;
            this.event = event;
            this.phase = phase;
        }

        @Override
        public int getOrder() {
            return this.listener.getOrder();
        }

        @Override
        public void beforeCommit(boolean readOnly) {
            if (this.phase == TransactionPhase.BEFORE_COMMIT) {
                processEvent();
            }
        }

        @Override
        public void afterCompletion(int status) {
            if (this.phase == TransactionPhase.AFTER_COMMIT && status == STATUS_COMMITTED) {
                processEvent();
            } else if (this.phase == TransactionPhase.AFTER_ROLLBACK && status == STATUS_ROLLED_BACK) {
                processEvent();
            } else if (this.phase == TransactionPhase.AFTER_COMPLETION) {
                processEvent();
            }
        }

        protected void processEvent() {
            this.listener.processEvent(this.event);
        }
    }
}
