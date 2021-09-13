package com.zebone.nhis.pro.zsrm.bl.vo;


/**
 * @author
 * @date 2021/05/31 10:10
 **/
public class PayRecordParamVo {
    /**
     * 商户订单号(至少选择一项进行条件筛选
     */
    private String outTradeNo;

    /**
     * 交易号
     */
    private String tradeNo;

    /**
     *交易时间格式：YYYY-MM-DD 24HH:MI:SS
     */
    private String timeStamp;

    /**
     *交易渠道 1：微信支付 2：支付宝
     */
    private String payMode;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }
}
