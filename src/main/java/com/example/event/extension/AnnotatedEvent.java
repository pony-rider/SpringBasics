package com.example.event.extension;

import lombok.ToString;
import org.springframework.context.PayloadApplicationEvent;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ToString
public class AnnotatedEvent<T> extends PayloadApplicationEvent<T> {
    private final List<Annotation> annotations;
    /**
     * Spring wraps simple object events into {@link PayloadApplicationEvent},
     * which requires {@code source} object on which the event initially occurred.
     * But we don't need it.
     * <p>Maybe {@link AnnotatedApplicationListenerMethodAdapter} should be rewritten with
     * custom logic of processing {@link AnnotatedEvent} objects analogously as {@link PayloadApplicationEvent}
     * in {@link org.springframework.context.event.ApplicationListenerMethodAdapter}
     */
    private static final Object DUMMY_SOURCE = new Object();

    public AnnotatedEvent(T payload, Annotation... annotations) {
        super(DUMMY_SOURCE, payload);
        this.annotations = Arrays.asList(annotations);
    }

    public AnnotatedEvent(Object source, T payload, Annotation... annotations) {
        super(source, payload);
        this.annotations = Arrays.asList(annotations);
    }


    public List<Annotation> getAnnotations() {
        return Collections.unmodifiableList(annotations);
    }
}

