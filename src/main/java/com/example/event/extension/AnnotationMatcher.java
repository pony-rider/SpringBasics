package com.example.event.extension;

import java.lang.annotation.Annotation;
import java.util.List;

public class AnnotationMatcher {

    //An observer method is notified if the set of observer qualifiers is a subset of the fired event’s qualifiers
    public static boolean qualifiersMatches(List<Annotation> listenerQualifiers, List<Annotation> eventQualifiers) {
        return eventQualifiers.containsAll(listenerQualifiers);
    }
}
