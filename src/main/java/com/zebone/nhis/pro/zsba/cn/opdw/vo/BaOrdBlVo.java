package com.zebone.nhis.pro.zsba.cn.opdw.vo;

import com.zebone.nhis.common.module.bl.opcg.BlOpDt;

public class BaOrdBlVo extends BlOpDt {
    private String dtPaymode;//支付方式

    public String getDtPaymode() {
        return dtPaymode;
    }

    public void setDtPaymode(String dtPaymode) {
        this.dtPaymode = dtPaymode;
    }
}
