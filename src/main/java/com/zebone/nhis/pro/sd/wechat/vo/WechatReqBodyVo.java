package com.zebone.nhis.pro.sd.wechat.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "body")
public class WechatReqBodyVo {
	@XmlElement(name = "branchCode")
	private String branchCode;

	/** 支付方式  weixin/alipay*/
	@XmlElement(name = "pay_method")
	private String pay_method;

	@XmlElement(name = "beginDate")
	private String beginDate;

	@XmlElement(name = "endDate")
	private String endDate;
	/** 订单类型*/
	@XmlElement(name = "orderType")
	private String orderType;
	/** 固定JY160*/
	@XmlElement(name = "Channel")
	private String Channel;
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getPay_method() {
		return pay_method;
	}
	public void setPay_method(String pay_method) {
		this.pay_method = pay_method;
	}

	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getChannel() {
		return Channel;
	}
	public void setChannel(String channel) {
		Channel = channel;
	}

}
