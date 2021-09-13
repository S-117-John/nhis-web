package com.zebone.nhis.webservice.pskq.annotation;

import java.lang.annotation.*;

/**
 * 职业
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Occupation {
    String value();
}
