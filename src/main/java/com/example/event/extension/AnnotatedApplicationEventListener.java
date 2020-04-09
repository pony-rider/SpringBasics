package com.example.event.extension;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ApplicationListenerMethodAdapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AnnotatedApplicationEventListener extends ApplicationListenerMethodAdapter {
    private List<Annotation> listenerQualifiers;

    public AnnotatedApplicationEventListener(String beanName, Class<?> targetClass, Method method) {
        super(beanName, targetClass, method);
        this.listenerQualifiers = method.getParameterCount() > 0 ?
                Arrays.asList(method.getParameterAnnotations()[0]) : Collections.emptyList();
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("event listener process event " + event);
        if (AnnotatedEvent.class.isAssignableFrom(event.getClass())) {
            AnnotatedEvent annotatedEvent = (AnnotatedEvent) event;
            boolean qualifiersMatches = AnnotationMatcher.qualifiersMatches(listenerQualifiers, annotatedEvent.getAnnotations());
            if (qualifiersMatches) {
                processEvent(event);
            }
        } else {
            if (listenerQualifiers.isEmpty()) {
                processEvent(event);
            }
        }
    }
}
