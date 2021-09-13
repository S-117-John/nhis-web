package com.zebone.nhis.webservice.pskq.annotation;

import java.lang.annotation.*;

/**
 * 民族
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Ethnic {

    String value();
}
