package com.example.event.extension;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationListenerMethodAdapter;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListenerDelegator extends ApplicationListenerMethodAdapter {
    private final List<Annotation> listenerQualifiers;
    private final GenericApplicationListener delegate;

    public ListenerDelegator(String beanName, Class<?> targetClass, Method method, GenericApplicationListener delegate) {
        super(beanName, targetClass, method);
        this.delegate = delegate;
        listenerQualifiers = method.getParameterCount() > 0 ?
                Arrays.asList(method.getParameterAnnotations()[0]) : Collections.emptyList();
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void onApplicationEvent(ApplicationEvent event) {
        if (AnnotatedEvent.class.isAssignableFrom(event.getClass())) {
            AnnotatedEvent annotatedEvent = (AnnotatedEvent) event;
            boolean qualifiersMatches = qualifiersMatches(annotatedEvent.getAnnotations());
            if (qualifiersMatches) {
                System.out.println(event);
                delegate.onApplicationEvent(event);
            }
        } else {
            if (listenerQualifiers.isEmpty()) {
                delegate.onApplicationEvent(event);
            }
        }
    }

    //An observer method is notified if the set of observer qualifiers is a subset of the fired event’s qualifiers
    private boolean qualifiersMatches(List<Annotation> eventQualifiers) {
        return eventQualifiers.containsAll(listenerQualifiers);
    }

    @Override
    public boolean supportsEventType(ResolvableType eventType) {
        return delegate.supportsEventType(eventType);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return delegate.supportsSourceType(sourceType);
    }

    @Override
    public int getOrder() {
        return delegate.getOrder();
    }
}
