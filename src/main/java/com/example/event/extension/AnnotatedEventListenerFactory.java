package com.example.event.extension;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.DefaultEventListenerFactory;

import java.lang.reflect.Method;

public class AnnotatedEventListenerFactory extends DefaultEventListenerFactory {
    private static final int ORDER = 49;

    @Override
    public ApplicationListener<?> createApplicationListener(String beanName, Class<?> type, Method method) {
        return new AnnotatedApplicationEventListener(beanName, type, method);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
