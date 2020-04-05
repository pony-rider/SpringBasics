package com.example.transaction;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration;
import org.springframework.transaction.interceptor.DelegatingTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

import java.lang.reflect.AnnotatedElement;

@Configuration
public class CustomRollbackProxyTransactionManagementConfiguration extends ProxyTransactionManagementConfiguration {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public TransactionAttributeSource transactionAttributeSource() {
        return new AnnotationTransactionAttributeSource() {

            //@Nullable
            protected TransactionAttribute determineTransactionAttribute(AnnotatedElement element) {
                TransactionAttribute ta = super.determineTransactionAttribute(element);
                if (ta == null) {
                    return null;
                } else {
                    return new DelegatingTransactionAttribute(ta) {
                        @Override
                        public boolean rollbackOn(Throwable ex) {
                            ExceptionRollback annotation = ex.getClass().getAnnotation(ExceptionRollback.class);
                            if (annotation != null) {
                                return annotation.rollback();
                            } else {
                                return super.rollbackOn(ex);
                            }
                        }
                    };
                }
            }
        };
    }
}

