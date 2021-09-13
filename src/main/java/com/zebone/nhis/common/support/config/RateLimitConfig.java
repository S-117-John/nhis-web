package com.zebone.nhis.common.support.config;

import cn.org.zxrl.config.EnableRateLimitConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(EnableRateLimitConfiguration.class)
@Configuration
public class RateLimitConfig {
    public static final String CUSTOM_PRX_ZSRM_API = "ZSRM_API_";
}