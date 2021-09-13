package com.zebone.nhis.cn.ipdw.aop;

import java.lang.annotation.*;

/**
 * 医嘱作废消息通知
 */
@Documented
@Inherited
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderVoid {

    String param();

    String type() default "";

    String pkCnOrd() default "";
}
