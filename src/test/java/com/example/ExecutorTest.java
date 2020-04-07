package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExecutorTest {
    @Qualifier("suggestVariantsExecutor")
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Qualifier("serviceExecutor")
    @Autowired
    private ThreadPoolTaskExecutor serviceExecutor;



    @Test
    public void test() {
        assertNotNull(taskExecutor);
        System.out.println(taskExecutor.getCorePoolSize());
        System.out.println(taskExecutor.getMaxPoolSize());
        System.out.println(taskExecutor.getKeepAliveSeconds());
    }
}
