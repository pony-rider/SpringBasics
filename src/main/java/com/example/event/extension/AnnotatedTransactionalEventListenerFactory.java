package com.example.event.extension;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListenerFactory;

import java.lang.reflect.Method;

@Component
public class AnnotatedTransactionalEventListenerFactory extends TransactionalEventListenerFactory {
    private static final int ORDER = 48;

    @Override
    public ApplicationListener<?> createApplicationListener(String beanName, Class<?> type, Method method) {
        return new AnnotatedTransactionalApplicationEventListener(beanName, type, method);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
