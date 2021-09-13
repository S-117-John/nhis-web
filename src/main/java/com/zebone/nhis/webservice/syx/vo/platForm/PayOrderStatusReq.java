package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PayOrderStatusReq {

	/**
     * 开始日期（YYYY-MM-DD）
     */
    @XmlElement(name = "startDate")
    private String startDate;
    
    /**
     * 结束日期（YYYY-MM-DD）
     */
    @XmlElement(name = "endDate")
    private String endDate;
    
    /**
     * 預約来源
     */
    @XmlElement(name = "orderType")
    private String orderType;
    
    /**
     * 用户证件类型
     */
    @XmlElement(name = "userCardType")
    private String userCardType;
    
    /**
     * 用户证件号码
     */
    @XmlElement(name = "userCardId")
    private String userCardId;
    
    /**
     * 外部预约系统的收费订单号
     */
    @XmlElement(name = "orderId")
    private String orderId;

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

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
    
	
	
}
