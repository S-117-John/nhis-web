package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PayBigOrderInfo {
	
	@XmlElement(name = "userCardType")
	private String userCardType;
	
	@XmlElement(name = "userCardId")
	private String userCardId;
	
	@XmlElement(name = "infoSeq")
	private String infoSeq;
	
	@XmlElement(name = "orderId")
	private String orderId;
	
	@XmlElement(name = "payCardNum")
	private String payCardNum;
	
	@XmlElement(name = "payAmout")
	private Double payAmout;
	
	@XmlElement(name = "payMode")
	private String payMode;
	
	@XmlElement(name = "payTime")
	private String payTime;
	
	@XmlElement(name = "orderDetailInfo")
	private List<OrderDetailInfo> orderDetailInfo;

	public String getUserCardType() {
		return userCardType;
	}

	public void setUserCardType(String userCardType) {
		this.userCardType = userCardType;
	}

	public String getUserCardId() {
		return userCardId;
	}

	public void setUserCardId(String userCardId) {
		this.userCardId = userCardId;
	}

	public String getInfoSeq() {
		return infoSeq;
	}

	public void setInfoSeq(String infoSeq) {
		this.infoSeq = infoSeq;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPayCardNum() {
		return payCardNum;
	}

	public void setPayCardNum(String payCardNum) {
		this.payCardNum = payCardNum;
	}

	public Double getPayAmout() {
		return payAmout;
	}

	public void setPayAmout(Double payAmout) {
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

	public List<OrderDetailInfo> getOrderDetailInfo() {
		return orderDetailInfo;
	}

	public void setOrderDetailInfo(List<OrderDetailInfo> orderDetailInfo) {
		this.orderDetailInfo = orderDetailInfo;
	}

}
