package com.zebone.nhis.ma.pub.platform.pskq.annotation;

import java.lang.annotation.*;

/**
 * 用于map转Object
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface MapKey {
    String value();
}
