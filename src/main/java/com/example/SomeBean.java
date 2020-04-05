package com.example;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SomeBean {
    private List<MySimpleEvent> simpleEvents = new ArrayList<>();

    public String getName() {
        return "someBean";
    }

    @EventListener
    public void handleEvent(MySimpleEvent event) {
        System.out.println("receive event: " + event);
        simpleEvents.add(event);
    }
}
