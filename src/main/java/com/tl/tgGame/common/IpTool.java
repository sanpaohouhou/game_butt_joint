package com.tl.tgGame.common;

import com.tl.tgGame.util.Ipv4Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Slf4j
@Service
public class IpTool {
    public String getIp() {
        return this.getIp(httpServletRequest);
    }

    public String getIp(HttpServletRequest request) {//获得客户端的IP,如果有更好的方法可以直接代替
        String ipAddress = "unknown";
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
            if (ipAddress != null && ipAddress.length() > 15 && ipAddress.indexOf(",") > 0) {
                List<String> ips = Arrays.asList(ipAddress.split(","));
                Collections.reverse(ips);
                for (final String ip : ips) {
                    if (!Ipv4Util.isInnerIP(ip.trim())) {
                        return ip;
                    }
                }
            }
        } catch (Exception e) {
            ipAddress = "unknown";
        }
        return ipAddress;
    }

    @Resource
    private HttpServletRequest httpServletRequest;
}
