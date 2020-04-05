package com.example;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Getter
public abstract class AbstractComponent {

    @Autowired
    private SomeBean someBean;

    private String property;

    @PostConstruct
    public void init() {
        property = "123";
    }
}
