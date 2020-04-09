package com.example.event.extension;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.EventListenerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

@Service
public class AnnotatedEventListenerFactory implements EventListenerFactory, Ordered {
    private static final int ORDER = 20;

    @Override
    public boolean supportsMethod(Method method) {
        System.out.println("calls support method from my factory");
        return AnnotatedElementUtils.hasAnnotation(method, EventListener.class);
    }

    @Override
    public ApplicationListener<?> createApplicationListener(String beanName, Class<?> type, Method method) {
        System.out.println("create custom event listener");
        return new AnnotatedApplicationListenerMethodAdapter(beanName, type, method);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
