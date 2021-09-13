package com.zebone.nhis.pro.zsba.mz.bl.vo;

import java.util.List;

public class PaymentCheckListVo {
    private List<PaymentCheckVo> paymentList;
    private int totalCount;

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
}
