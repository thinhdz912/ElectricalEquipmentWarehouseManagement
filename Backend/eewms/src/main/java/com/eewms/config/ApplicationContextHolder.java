// src/main/java/com/inventorymanagement/config/ApplicationContextHolder.java
package com.eewms.config;

import org.springframework.context.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHolder implements ApplicationContextAware {
    private static ApplicationContext ctx;

    @Override
    public synchronized void setApplicationContext(ApplicationContext applicationContext) {
        if (ctx == null) ctx = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return ctx.getBean(clazz);
    }
}
