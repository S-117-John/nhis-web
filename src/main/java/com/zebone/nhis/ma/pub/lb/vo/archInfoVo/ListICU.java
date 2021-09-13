/**
  * Copyright 2021 json.cn 
  */
package com.zebone.nhis.ma.pub.lb.vo.archInfoVo;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 *
 */
public class ListICU {

    @JSONField(name = "ZZJHSMC")
    private String zZJHSMC;
    @JSONField(name = "JRZZJHSSJ")
    private Date jRZZJHSSJ;
    @JSONField(name = "ZCZZJHSSJ")
    private Date zCZZJHSSJ;

    public String getzZJHSMC() {
        return zZJHSMC;
    }

    public void setzZJHSMC(String zZJHSMC) {
        this.zZJHSMC = zZJHSMC;
    }

    public Date getjRZZJHSSJ() {
        return jRZZJHSSJ;
    }

    public void setjRZZJHSSJ(Date jRZZJHSSJ) {
        this.jRZZJHSSJ = jRZZJHSSJ;
    }

    public Date getzCZZJHSSJ() {
        return zCZZJHSSJ;
    }

    public void setzCZZJHSSJ(Date zCZZJHSSJ) {
        this.zCZZJHSSJ = zCZZJHSSJ;
    }
}