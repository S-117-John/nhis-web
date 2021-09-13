package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RegListInfo {
	
	@XmlElement(name = "orderIdHis")
	private String orderIdHis;
	
	@XmlElement(name = "userFlag")
	private String userFlag;
	
	@XmlElement(name = "waitTime")
	private String waitTime;
	
	@XmlElement(name = "diagnoseRoomName")
	private String diagnoseRoomName;
	
	@XmlElement(name = "status")
	private String status;
	
	@XmlElement(name = "orderType")
    private String orderType;
    
	@XmlElement(name = "orderId")
    private String orderId;
        
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

	public String getOrderIdHis() {
		return orderIdHis;
	}

	public void setOrderIdHis(String orderIdHis) {
		this.orderIdHis = orderIdHis;
	}

	public String getUserFlag() {
		return userFlag;
	}

	public void setUserFlag(String userFlag) {
		this.userFlag = userFlag;
	}

	public String getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}

	public String getDiagnoseRoomName() {
		return diagnoseRoomName;
	}

	public void setDiagnoseRoomName(String diagnoseRoomName) {
		this.diagnoseRoomName = diagnoseRoomName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
