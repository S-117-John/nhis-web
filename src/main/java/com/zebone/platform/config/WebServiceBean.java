package com.zebone.platform.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 读取ws配置，为空会异常
 */
@Component
@ConfigurationProperties(prefix = "ws")
public class WebServiceBean {

    private Map<String,String> route;

    public Map<String, String> getRoute() {
        return route;
    }

    public void setRoute(Map<String, String> route) {
        this.route = route;
    }
}
