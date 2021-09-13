package com.zebone.nhis.webservice.support;

public class OtherRespJsons {

    //外部订单号
    private String TradeNo;
    //订单号
    private String outTradeNo;
    //交易金额
    private String TotalAmount;
    //买家付款的金额
    private String PaymentAmount;
    //返回值
    private String ReturnCode;
    //返回信息
    private String ReturnMsg;

    public String getTradeNo() {
        return TradeNo;
    }

    public void setTradeNo(String tradeNo) {
        TradeNo = tradeNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getPaymentAmount() {
        return PaymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        PaymentAmount = paymentAmount;
    }

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String returnCode) {
        ReturnCode = returnCode;
    }

    public String getReturnMsg() {
        return ReturnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        ReturnMsg = returnMsg;
    }

    @Override
    public String toString() {
        return "{\"TradeNo\":\"" + TradeNo + "\", \"outTradeNo\":\"" + outTradeNo + "\" , \"TotalAmount\":\"" + TotalAmount+ "\" , \"PaymentAmount\":\"" + PaymentAmount + "\""
                +" ,\"ReturnCode\":\""+ ReturnCode + "\" , \"ReturnMsg\":\"" + ReturnMsg +"\"}";

    }
}
