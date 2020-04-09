package com.example.event.extension;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EventListener
public @interface AnnotatedEventListener {

    /**
     * Alias for {@link #classes}.
     */
    @AliasFor(annotation = EventListener.class, attribute = "classes")
    Class<?>[] value() default {};

    /**
     * The event classes that this listener handles.
     * <p>If this attribute is specified with a single value, the annotated
     * method may optionally accept a single parameter. However, if this
     * attribute is specified with multiple values, the annotated method
     * must <em>not</em> declare any parameters.
     */
    @AliasFor(annotation = EventListener.class, attribute = "classes")
    Class<?>[] classes() default {};

    /**
     * Spring Expression Language (SpEL) attribute used for making the event
     * handling conditional.
     * <p>The default is {@code ""}, meaning the event is always handled.
     * @see EventListener#condition
     */
    String condition() default "";
}
