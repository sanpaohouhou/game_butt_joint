package com.tl.tgGame.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class RedisKeyGenerator {
    @Value("${spring.application.name}")
    private String applicationName;

    public String generateKey(String baseName) {
        return String.join(":", applicationName, baseName);
    }

    public String generateKey(String... segments) {
        return generateKey(String.join(":", segments));
    }
    public String generateKey(Object... segments) {
        return generateKey(Arrays.stream(segments).map(Object::toString).collect(Collectors.joining(":")));
    }
}
