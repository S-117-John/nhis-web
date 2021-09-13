package com.zebone.nhis.webservice.pskq.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DuplicateCommitLimit {

    /**
     * 拦截时间，单位为秒，默认值1秒
     */
    long time() default 1;
}
