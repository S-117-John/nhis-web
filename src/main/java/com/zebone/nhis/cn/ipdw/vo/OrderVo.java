package com.zebone.nhis.cn.ipdw.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;

public class OrderVo extends CnOrder {

    /**草药处方名称**/
    private String namePres;

    public String getNamePres() {
        return namePres;
    }

    public void setNamePres(String namePres) {
        this.namePres = namePres;
    }
}
