package com.tl.tgGame.common.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tl.tgGame.common.IpTool;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;


@WebFilter(urlPatterns = "/api/*")
public class PrintLogFilter implements Filter {
    @Resource
    private IpTool ipTool;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ContentCachingRequestWrapper cachingServletRequest = new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
        ContentCachingResponseWrapper cachingServletResponse = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);
        Long s = System.currentTimeMillis();
        filterChain.doFilter(cachingServletRequest, cachingServletResponse);
        Long e = System.currentTimeMillis();

        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("requestTime", s);
        map.put("duration", e - s);
        map.put("remote", ipTool.getIp((HttpServletRequest) servletRequest));
        map.put("requestPath", cachingServletRequest.getRequestURI());
        map.put("requestHeaders", HeadersExtractor.extract(cachingServletRequest));
        map.put("requestBody", new String(cachingServletRequest.getContentAsByteArray()));
        map.put("responseHeaders", HeadersExtractor.extract(cachingServletResponse));
        map.put("responseBody", new String(cachingServletResponse.getContentAsByteArray()));
        map.put("responseStatus", cachingServletResponse.getStatus());

        System.out.println(new ObjectMapper().writeValueAsString(map));
        cachingServletResponse.copyBodyToResponse();
    }
}
