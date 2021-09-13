package com.zebone.nhis.common.support.anno;

import java.lang.annotation.*;

/**
 * 阻止重复提交请求
 */
@Documented
@Inherited
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SameUrlData {

    /**
     * 客户端id
     * @return
     */
    String clientId() default "";

    /**
     * 时间间隔，单位：分钟
     * @return
     */
    int minutes() default 10;
}
