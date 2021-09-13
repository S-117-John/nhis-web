package com.zebone.nhis.webservice.pskq.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ContactRelationship {

    String value();
}
