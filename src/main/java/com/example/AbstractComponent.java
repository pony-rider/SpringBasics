package com.example;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Getter
public abstract class AbstractComponent {

    private String property;

    @PostConstruct
    public void init() {
        property = "123";
    }
}
