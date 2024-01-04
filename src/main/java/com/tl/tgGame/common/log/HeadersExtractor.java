package com.tl.tgGame.common.log;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;

public class HeadersExtractor {

    public static LinkedHashMap<String, ArrayList<String>> extract(ServletRequest servletRequest) {
        HttpServletRequest httpServletRequest = ((HttpServletRequest) servletRequest);
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        LinkedHashMap<String, ArrayList<String>> headers = new LinkedHashMap<>();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                ArrayList<String> value = new ArrayList<>();
                Enumeration<String> headerValues = httpServletRequest.getHeaders(name);
                if (headerValues != null) {
                    while (headerValues.hasMoreElements()) {
                        value.add(headerValues.nextElement());
                    }
                }
                headers.put(name, value);
            }
        }
        return headers;
    }

    public static LinkedHashMap<String, ArrayList<String>> extract(ServletResponse servletResponse) {
        HttpServletResponse httpServletResponse = ((HttpServletResponse) servletResponse);
        Collection<String> headerNames = httpServletResponse.getHeaderNames();
        LinkedHashMap<String, ArrayList<String>> headers = new LinkedHashMap<>();
        if (headerNames != null) {
            headerNames.forEach(name -> {
                Collection<String> headerValues = httpServletResponse.getHeaders(name);
                headers.put(name, headerValues == null ? new ArrayList<>() : new ArrayList<>(headerValues));
            });
        }
        return headers;
    }
}
