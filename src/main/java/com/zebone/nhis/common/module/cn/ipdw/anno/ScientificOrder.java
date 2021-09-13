package com.zebone.nhis.common.module.cn.ipdw.anno;


import java.lang.annotation.*;

/**
 * 拦截医嘱中的科研医嘱
 */
@Documented
@Inherited
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ScientificOrder {
}
