package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PayOrder {
	
	@XmlElement(name = "orderType")
	private String orderType;
	
	@XmlElement(name = "orderId")
	private String orderId;
	
	@XmlElement(name = "payNum")
	private String payNum;
	
	@XmlElement(name = "payAmout")
	private Double payAmout;
	
	@XmlElement(name = "payTime")
	private String payTime;
	
	@XmlElement(name = "payDesc")
	private String payDesc;
	
	@XmlElement(name = "returnNum")
	private String returnNum;
	
	@XmlElement(name = "returnAmout")
	private Double returnAmout;
	
	@XmlElement(name = "returnTime")
	private Date returnTime;
	
	@XmlElement(name = "returnDesc")
	private String returnDesc;

	
	public String getReturnNum() {
		return returnNum;
	}

	public void setReturnNum(String returnNum) {
		this.returnNum = returnNum;
	}

	public Double getReturnAmout() {
		return returnAmout;
	}

	public void setReturnAmout(Double returnAmout) {
		this.returnAmout = returnAmout;
	}

	public Date getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}

	public String getReturnDesc() {
		return returnDesc;
	}

	public void setReturnDesc(String returnDesc) {
		this.returnDesc = returnDesc;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPayNum() {
		return payNum;
	}

	public void setPayNum(String payNum) {
		this.payNum = payNum;
	}

	public Double getPayAmout() {
		return payAmout;
	}

	public void setPayAmout(Double payAmout) {
		this.payAmout = payAmout;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getPayDesc() {
		return payDesc;
	}

	public void setPayDesc(String payDesc) {
		this.payDesc = payDesc;
	}
	
}
