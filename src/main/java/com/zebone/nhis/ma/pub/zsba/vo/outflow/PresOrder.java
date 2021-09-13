package com.zebone.nhis.ma.pub.zsba.vo.outflow;

public class PresOrder {

    /** 就诊记录id 医疗机构	string	36	是	就诊记录id*/
    private String visitIdOutter;

    /** 外部机构处方id医疗机构	string	500	否	多个处方id以英文逗号分隔*/
    private String recipeIdOutter;

    /** 订单状态	string	2	否	noticeType=10时返回 05=已完成 （订单最终状态将会通知到医疗机构）*/
    private String orderStatus;
    /** 通知类型	string	2	是	10= 处方流转通知，11=诊金缴费通知*/
    private String noticeType;
    /** 支付结果	int	2	否	noticeType=11时返回  支付结果：0=未支付，1=已支付*/
    private Integer payStatus;
    /** 支付时间	string	16	否	noticeType=11时返回  日期格式：yyyy-MM-dd HH:mm:ss*/
    private String payTime;
    /** 支付金额	double	8,2	否	noticeType=11时返回 单位：元*/
    private Double payAmt;

    /** 业务类型	int	2	是	1=诊金缴费*/
    private String bizType;

    public String getVisitIdOutter() {
        return visitIdOutter;
    }

    public void setVisitIdOutter(String visitIdOutter) {
        this.visitIdOutter = visitIdOutter;
    }

    public String getRecipeIdOutter() {
        return recipeIdOutter;
    }

    public void setRecipeIdOutter(String recipeIdOutter) {
        this.recipeIdOutter = recipeIdOutter;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public Double getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(Double payAmt) {
        this.payAmt = payAmt;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }
}
