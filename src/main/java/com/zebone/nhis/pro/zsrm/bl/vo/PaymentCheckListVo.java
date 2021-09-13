package com.zebone.nhis.pro.zsrm.bl.vo;

import java.util.List;

public class PaymentCheckListVo {
    private List<PaymentCheckVo> paymentList;
    private int totalCount;

    //一代交易数总数
    private String oneInsuSum;
    //一代交易总金额
    private String oneInsuAmt;
    //三代交易数总数
    private String threInsuSum;
    //三代交易总金额
    private String threInsuAmt;
    //交易总条数
    private String totalSum;
    //交易总金额
    private String totalAmt;

    public List<PaymentCheckVo> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<PaymentCheckVo> paymentList) {
        this.paymentList = paymentList;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getOneInsuSum() {
        return oneInsuSum;
    }

    public void setOneInsuSum(String oneInsuSum) {
        this.oneInsuSum = oneInsuSum;
    }

    public String getOneInsuAmt() {
        return oneInsuAmt;
    }

    public void setOneInsuAmt(String oneInsuAmt) {
        this.oneInsuAmt = oneInsuAmt;
    }

    public String getThreInsuSum() {
        return threInsuSum;
    }

    public void setThreInsuSum(String threInsuSum) {
        this.threInsuSum = threInsuSum;
    }

    public String getThreInsuAmt() {
        return threInsuAmt;
    }

    public void setThreInsuAmt(String threInsuAmt) {
        this.threInsuAmt = threInsuAmt;
    }

    public String getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(String totalSum) {
        this.totalSum = totalSum;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }
}
