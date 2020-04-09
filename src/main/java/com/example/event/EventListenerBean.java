package com.example.event;

import com.example.event.annotations.MyQualifier;
import com.example.event.extension.AnnotatedEvent;
import com.example.event.extension.AnnotatedEventListener;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class EventListenerBean {
    private List<MySimpleEvent> simpleEvents = new ArrayList<>();
    private List<MySimpleEvent> childEvents = new ArrayList<>();
    private List<AnnotatedEvent> customEvents = new ArrayList<>();
    private List<Object> customEventSources = new ArrayList<>();
    private int eventCounter;
    private int customEventCounter;

    public String getName() {
        return "someBean";
    }

   /* @EventListener
    public void handleEvent(MySimpleEvent event) {
        System.out.println("receive event: " + event);
        simpleEvents.add(event);
    }

    @EventListener
    public void handleCustomEvent(@MyQualifier("123") AnnotatedEvent event) {
        System.out.println("receive custom event: " + event + " " + event.getAnnotations().size());
        customEvents.add(event);
    }*/

    @EventListener
    public void handleCustomEventSource(@MyQualifier("123") String object) {
        System.out.println("receive custom event: " + object);
        customEventSources.add(object);
    }

    @EventListener
    public void handleAllEvent(ApplicationEvent event) {
        System.out.println("receive any event: " + event + " " + event.getSource().getClass());
        eventCounter++;
    }

    @EventListener
    public void handleAllCustomEventSource(String object) {
        System.out.println("receive string event: " + object);
        customEventCounter++;
    }


   /* @TransactionalEventListener
    public void handleStringEvent(ApplicationEvent event) {
        System.out.println("receive transactional event: " + event);
    }*/
}
