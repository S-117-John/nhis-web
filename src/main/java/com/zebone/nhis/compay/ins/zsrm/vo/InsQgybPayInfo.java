package com.zebone.nhis.compay.ins.zsrm.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

public class InsQgybPayInfo {
    @JSONField(name = "fund_pay_type")
    private String fundPayType;
    @JSONField(name = "fund_payamt")
    private BigDecimal fundPayamt;

    public String getFundPayType() {
        return fundPayType;
    }

    public void setFundPayType(String fundPayType) {
        this.fundPayType = fundPayType;
    }

    public BigDecimal getFundPayamt() {
        return fundPayamt;
    }

    public void setFundPayamt(BigDecimal fundPayamt) {
        this.fundPayamt = fundPayamt;
    }
}
