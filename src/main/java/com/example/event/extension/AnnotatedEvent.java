package com.example.event.extension;

import org.springframework.context.PayloadApplicationEvent;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AnnotatedEvent<T> extends PayloadApplicationEvent<T> {
    private final List<Annotation> annotations;

    public AnnotatedEvent(Object source, T payload, Annotation... annotations) {
        super(source, payload);
        this.annotations = Arrays.asList(annotations);
    }

    public List<Annotation> getAnnotations() {
        return Collections.unmodifiableList(annotations);
    }

    @Override
    public String toString() {
        return "AnnotatedEvent{" +
                "payload=" + getPayload() +
                ", annotations=" + annotations +
                ", source=" + getSource() +
                '}';
    }
}

