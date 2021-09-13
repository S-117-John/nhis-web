package com.zebone.nhis.compay.ins.qgyb.anno;

import java.lang.annotation.*;

/**
 * @author 卡卡西
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FileMapping {

    int serialNo();

    String Table1301();

    String Table1302();

    String Table1303();

    String Table1304();

    String Table1305();

    String Table1306();
}
