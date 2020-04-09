package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChildComponentTest {
    @Autowired
    private ChildComponent childComponent;

    @Test
    public void test() {
        childComponent.fireSimpleEvent();
        assertNotNull(childComponent.getEventListenerBean().getName());
        assertNotNull(childComponent.getProperty());
        System.out.println(childComponent.getEventListenerBean().getName());
        System.out.println(childComponent.getProperty());
    }



}
