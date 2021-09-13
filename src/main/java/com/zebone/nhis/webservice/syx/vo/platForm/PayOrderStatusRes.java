package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "res")
public class PayOrderStatusRes {

	/**
     * HIS缴费订单号
     */
    @XmlElement(name = "orderIdHIS")
    private String orderIdHIS;
    
    /**
     * 支付金额
     */
    @XmlElement(name = "payAmout")
    private String payAmout;
    
    /**
     * 支付方式
     */
    @XmlElement(name = "payMode")
    private String payMode;
    
    /**
     * 交易时间
     */
    @XmlElement(name = "payTime")
    private String payTime;
    
    /**
     * 订单相关信息描述
     */
    @XmlElement(name = "orderDesc")
    private String orderDesc;

	public String getOrderIdHIS() {
		return orderIdHIS;
	}

	public void setOrderIdHIS(String orderIdHIS) {
		this.orderIdHIS = orderIdHIS;
	}

	public String getPayAmout() {
		return payAmout;
	}

	public void setPayAmout(String payAmout) {
		this.payAmout = payAmout;
	}

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getOrderDesc() {
		return orderDesc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}
    
    
}
