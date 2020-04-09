package com.example;

import com.example.event.extension.AnnotatedApplicationEventPublisher;
import com.example.event.extension.AnnotatedEventListenerFactory;
import com.example.event.test.EventPublisherBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
public class Application {

    @Autowired
    private ActionBean readAction;
    @Autowired
    private ActionBean writeAction;

    @Bean
    public ActionBean readAction() {
        return new ActionBean("read");
    }

    @Bean
    public ActionBean writeAction() {
        return new ActionBean("write");
    }

    @Bean("suggestVariantsExecutor")
    public ThreadPoolTaskExecutor getSuggestVariantsExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1);
        return taskExecutor;
    }

    @Bean("serviceExecutor")
    public ThreadPoolTaskExecutor getServiceExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(2);
        return taskExecutor;
    }

    @Bean("timersScheduler")
    public ThreadPoolTaskScheduler getTimerScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(3);
        return taskScheduler;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


}
