package com.zebone.nhis.pro.zsrm.cn.vo;

import com.zebone.nhis.common.module.pi.PiMaster;


public class PiZsVo extends PiMaster {
    private String cardPv;//诊疗卡号
    private String pkHp;//医保类型

    public String getCardPv() {
        return cardPv;
    }

    public void setCardPv(String cardPv) {
        this.cardPv = cardPv;
    }

    public String getPkHp() {
        return pkHp;
    }

    public void setPkHp(String pkHp) {
        this.pkHp = pkHp;
    }
}


