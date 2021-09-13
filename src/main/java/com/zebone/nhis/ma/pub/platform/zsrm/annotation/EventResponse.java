package com.zebone.nhis.ma.pub.platform.zsrm.annotation;

import java.lang.annotation.*;

/**
 * 标识为ResponseProcessor的实现
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventResponse {
    String value();
}
