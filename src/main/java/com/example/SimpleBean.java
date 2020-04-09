package com.example;

import com.example.event.MySimpleEvent;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class SimpleBean {
    private String property = "1";
    private List<MySimpleEvent> simpleEvents = new ArrayList<>();
}
