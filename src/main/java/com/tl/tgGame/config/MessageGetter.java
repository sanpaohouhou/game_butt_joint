package com.tl.tgGame.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class MessageGetter {

    @Resource
    private MessageSource messageSource;

    public String getMessage(String key, @Nullable Object[] objects) {
        log.info("获取Message Key: {} Language: {}", key, LocaleContextHolder.getLocale());
        return messageSource.getMessage(key, objects, LocaleContextHolder.getLocale());
    }

    public String getOrDefault(String key, String s) {
        try {
            return getMessage(key, null);
        } catch (NoSuchMessageException ignored) {
        }
        return s;
    }

    public String getOrDefault(String key, String s, Object... objects) {
        try {
            return getMessage(key, objects);
        } catch (NoSuchMessageException ignored) {
        }
        return s;
    }

    public String get(String key) {
        return getOrDefault(key, "");
    }

    public String get(String key, Object... objects) {
        return getOrDefault(key, "", objects);
    }
}
