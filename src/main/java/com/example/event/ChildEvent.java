package com.example.event;

import org.springframework.beans.factory.annotation.Qualifier;

@Qualifier("child-event")
public class ChildEvent extends MySimpleEvent {
    public ChildEvent(String text) {
        super(text);
    }
}
