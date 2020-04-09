package com.example.event.extension;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ApplicationListenerMethodAdapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AnnotatedApplicationListenerMethodAdapter extends ApplicationListenerMethodAdapter {
    private List<Annotation> listenerQualifiers;

    public AnnotatedApplicationListenerMethodAdapter(String beanName, Class<?> targetClass, Method method) {
        super(beanName, targetClass, method);
        listenerQualifiers = method.getParameterCount() > 0 ?
                Arrays.asList(method.getParameterAnnotations()[0]) : Collections.emptyList();
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (AnnotatedEvent.class.isAssignableFrom(event.getClass())) {
            AnnotatedEvent annotatedEvent = (AnnotatedEvent) event;
            boolean qualifiersMatches = qualifiersMatches(annotatedEvent.getAnnotations());
            if (qualifiersMatches) {
                super.onApplicationEvent(event);
            }
        } else {
            if (listenerQualifiers.isEmpty()) {
                super.onApplicationEvent(event);
            }
        }
    }

    //An observer method is notified if the set of observer qualifiers is a subset of the fired event’s qualifiers
    private boolean qualifiersMatches(List<Annotation> eventQualifiers) {
        return eventQualifiers.containsAll(listenerQualifiers);
    }
}
