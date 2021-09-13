package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="req")
@XmlAccessorType(XmlAccessType.FIELD)
public class QryCheckListInfo {

    /**
     * 付款来源
     * 1-健康之路
     * 2-EMEM
     * 3-电话（114）
     * 7-农行网点
     * 8-医享网微信
     * 9-医享网支付宝
     * 11-平安挂号
     * 13-翼健康
     * 40-医程通
     * 58-华医App
     * 59-平安APP
     */
    @XmlElement(name="paySource")
    private String paySource;
    /**
     * 支付方式：
     *      * 1	现金
     *      * 2	支票
     *      * 3	银行卡
     *      * 4	患者账户
     *      * 5	汇票
     *      * 7	微信
     *      * 8	支付宝
     *      * 59-平安APP
     *      * 99其他
     */
    @XmlElement(name="payMode")
    private String payMode;
    /**
     * 订单类型：
     * 1-住院押金缴纳
     */
    @XmlElement(name="type")
    private String type;
    /**
     * 开始日期，
     * 格式：YYYY-MM-DD
     */
    @XmlElement(name="startDate")
    private String startDate;
    /**
     * 结束日期
     * 格式：YYYY-MM-DD
     */
    @XmlElement(name="endDate")
    private String endDate;

    public String getPaySource() {
        return paySource;
    }

    public void setPaySource(String paySource) {
        this.paySource = paySource;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
