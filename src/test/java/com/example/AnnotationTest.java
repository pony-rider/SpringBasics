package com.example;

import com.example.event.EventListenerBean;
import com.example.event.annotations.MyQualifier;
import com.example.event.extension.AnnotatedEvent;
import org.junit.Test;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.core.annotation.AnnotationUtils.synthesizeAnnotation;

public class AnnotationTest {
    @Test
    public void test() throws NoSuchMethodException {
        /*Method method = Test.class.getDeclaredMethod
                ("testMethod", String.class, String.class);*/
        //AnnotationUtils.
        Class<?> targetClass = EventListenerBean.class;
        Method method = targetClass.getMethod("handleCustomEvent", AnnotatedEvent.class);
        List<Annotation> annotations = Arrays.asList(method.getParameterAnnotations()[0]);
        System.out.println(annotations);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("count", 0);
        attributes.put("value", "123");
        Annotation synthesizedAnnotation = AnnotationUtils.synthesizeAnnotation(attributes, MyQualifier.class, null);
        List<Annotation> expectedAnnotations = Arrays.asList(synthesizedAnnotation);
        System.out.println(expectedAnnotations);
        //assertEquals(expectedAnnotations, annotations);
        assertEquals(annotations.get(0), synthesizedAnnotation);
    }

    @Test
    public void test_collection() {
        List<String> empty = Collections.emptyList();
        List<String> strings = Collections.emptyList();//Arrays.asList();
        assertTrue(strings.containsAll(empty));
    }
}
