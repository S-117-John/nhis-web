package com.zebone.nhis.ma.pub.platform.zsrm.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventSearch {

    String value();
}
