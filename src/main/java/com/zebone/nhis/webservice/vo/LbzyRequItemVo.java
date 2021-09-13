package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbzyRequItemVo {
    //支付类型代码
    @XmlElement(name="PayTypeID")
    private String payTypeID;
    //POS交易信息	1.银行传入： POS流水号|批次号|参考号|POS终端编号|商务号|日期|时间
    //              2.支付宝微信传入：0|订单号|支付交易流水号
    @XmlElement(name="POSTransNO")
    private String pOSTransNO;
    //金额
    @XmlElement(name="Amount")
    private String amount;

    public String getPayTypeID() {
        return payTypeID;
    }

    public void setPayTypeID(String payTypeID) {
        this.payTypeID = payTypeID;
    }

    public String getpOSTransNO() {
        return pOSTransNO;
    }

    public void setpOSTransNO(String pOSTransNO) {
        this.pOSTransNO = pOSTransNO;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
