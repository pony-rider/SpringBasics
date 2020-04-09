package com.example.event.extension;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Map;

public class AnnotationUtils {

    public static <T extends Annotation> T createAnnotation(Class<T> type, Object value) {
        Map<String, Object> attributes = Collections.singletonMap("value", value);
        return org.springframework.core.annotation.AnnotationUtils.synthesizeAnnotation(attributes, type, null);
    }

    public static <T extends Annotation> T createAnnotation(Class<T> type, Map<String, Object> attributes) {
        return org.springframework.core.annotation.AnnotationUtils.synthesizeAnnotation(attributes, type, null);
    }
}
