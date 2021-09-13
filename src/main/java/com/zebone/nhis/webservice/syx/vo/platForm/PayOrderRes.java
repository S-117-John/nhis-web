package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PayOrderRes {
	@XmlElement(name = "resultDesc")
	private String resultDesc;
	
	@XmlElement(name = "resultCode")
	private String resultCode;
	
	@XmlElement(name = "orderIdHis")
	private String orderIdHis;		
	
	public String getOrderIdHis() {
		return orderIdHis;
	}
	public void setOrderIdHis(String orderIdHis) {
		this.orderIdHis = orderIdHis;
	}
	public String getResultDesc() {
		return resultDesc;
	}
	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	
	
}
