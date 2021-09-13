package com.zebone.nhis.webservice.pskq.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MetadataDescribe {

    String id();

    String name();

    String eName();

    String property() default "";
}
