package com.tl.tgGame.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextTool implements ApplicationContextAware {

    private static volatile ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextTool.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> c) {
        if (applicationContext == null) return null;
        return applicationContext.getBean(c);
    }

    public static <T> T getBean(String name, Class<T> c) {
        if (applicationContext == null) return null;
        return applicationContext.getBean(name, c);
    }
}
