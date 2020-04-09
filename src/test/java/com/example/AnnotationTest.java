package com.example;

import org.junit.Test;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AnnotationTest {
    @Test
    public void test() throws NoSuchMethodException {
        Class<?> targetClass = EventListenerBean.class;
        Method method = targetClass.getMethod("handleCustomEvent", String.class);
        List<Annotation> annotations = Arrays.asList(method.getParameterAnnotations()[0]);
        System.out.println(annotations);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("count", 1);
        attributes.put("value", "123");
        Annotation synthesizedAnnotation = AnnotationUtils.synthesizeAnnotation(attributes, MyQualifier.class, null);
        List<Annotation> expectedAnnotations = Arrays.asList(synthesizedAnnotation);
        System.out.println(expectedAnnotations);
        //assertEquals(expectedAnnotations, annotations);
        assertEquals(annotations.get(0), synthesizedAnnotation);
    }

    @Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface MyQualifier {
        String value();

        int count() default 0;
    }

    static class EventListenerBean {
        public void handleCustomEvent(@MyQualifier(value = "123", count = 1) String message) {
        }
    }

    @Test
    public void test_collection() {
        List<String> empty = Collections.emptyList();
        List<String> strings = Collections.emptyList();//Arrays.asList();
        assertTrue(strings.containsAll(empty));
    }
}
