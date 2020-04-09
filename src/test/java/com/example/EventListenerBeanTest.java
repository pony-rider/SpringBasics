package com.example;

import com.example.event.EventListenerBean;
import com.example.event.EventPublishingBean;
import com.example.event.annotations.MyQualifier;
import com.example.event.extension.AnnotatedApplicationEventPublisher;
import com.example.event.extension.AnnotationUtils;
import com.example.event.extension.AnnotatedEventListenerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.EventListenerFactory;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventListenerBeanTest {
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private EventPublishingBean eventPublishingBean;

    @Autowired
    private EventListenerBean eventListenerBean;

    @Autowired
    private AnnotatedEventListenerFactory annotatedEventListenerFactory;

    @Autowired
    private ConfigurableListableBeanFactory beanFactory;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AnnotatedApplicationEventPublisher applicationEventPublisher;

    @Test
    public void testSimpleEvents() {
        eventPublishingBean.fireSimpleEvent();
        eventPublishingBean.fireChildEvent();
        applicationEventPublisher.publishEvent("string event");
        assertEquals(2, eventListenerBean.getSimpleEvents().size());
        //assertEquals(3, eventListenerBean.getEventCounter());
    }

    @Test
    public void testSCustomEvents() {
        MyQualifier myQualifier1 = AnnotationUtils.createAnnotation(MyQualifier.class, "123");
        MyQualifier myQualifier2 = AnnotationUtils.createAnnotation(MyQualifier.class, "1234");
        applicationEventPublisher.publishEvent("123", myQualifier1);
        applicationEventPublisher.publishEvent("1234", myQualifier2);

        //assertEquals(2, eventListenerBean.getCustomEvents().size());
        applicationEventPublisher.publishEvent("text");
        applicationEventPublisher.publishEvent(1);
        System.out.println(eventListenerBean.getEventCounter());
        System.out.println(eventListenerBean.getCustomEventCounter());
        //assertEquals(2, eventListenerBean.getCustomEventSources().size());
        //System.out.println(eventListenerBean.getCustomEventSources());
        //System.out.println(eventListenerBean.getCustomEvents());
    }



    @Test
    public void myEventListenerFactoryTest() {
        //checkMethodSupport();
        //printApplicationListeners();
        assertNotNull(annotatedEventListenerFactory);
        //assertEquals(2, myEventListenerFactory.getProperty());
        //myEventListenerFactory.createApplicationListener(null, null, null);
    }
    /*eventPublishingBean.fireGenericEvent("some text");
        eventPublishingBean.fireGenericEvent(1);
        assertEquals(1, eventListenerBean.getStringGenericEvents().size());
        assertEquals(1, eventListenerBean.getIntGenericEvents().size());*/

    private void checkMethodSupport() {
        Map<Method, EventListener> annotatedMethods = null;
        //Class<?> targetType = AutoProxyUtils.determineTargetClass(beanFactory, beanName);
        try {
            annotatedMethods = MethodIntrospector.selectMethods(EventListenerBean.class,
                    (MethodIntrospector.MetadataLookup<EventListener>) method ->
                            AnnotatedElementUtils.findMergedAnnotation(method, EventListener.class));
        } catch (Throwable ex) {
            // An unresolvable type in a method signature, probably from a lazy bean - let's ignore it.
            if (logger.isDebugEnabled()) {
                logger.debug("Could not resolve methods for bean with name '" + EventListenerBean.class.toString() + "'", ex);
            }
        }
        //beanFactory.(beanFactory.getBean(AnnotationConfigUtils.EVENT_LISTENER_FACTORY_BEAN_NAME));
        Map<String, EventListenerFactory> beans = beanFactory.getBeansOfType(EventListenerFactory.class, false, false);
        List<EventListenerFactory> factories = new ArrayList<>(beans.values());
        for (EventListenerFactory factory: factories) {
            for (Method method : annotatedMethods.keySet()) {
                boolean supports = factory.supportsMethod(method);
                System.out.println(factory.getClass() + " " + method.getName() + " " + supports);
            }
        }

        //ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;


    }

    private void printApplicationListeners() {
        System.out.println(applicationContext);
        //System.out.println(applicationContext.get);
        String[] beans = applicationContext.getBeanDefinitionNames();
        Stream.of(beans).forEach(System.out::println);
        //System.out.println(Arrays.toString());
    }
}
