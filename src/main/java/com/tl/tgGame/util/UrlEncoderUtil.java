package com.tl.tgGame.util;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @version 1.0
 * @auther w
 * @date 2024/1/24 , 15:31
 */
@Component
public class UrlEncoderUtil {


    public static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    public static String decode(String value){
        return URLDecoder.decode(value,StandardCharsets.UTF_8);
    }
}
