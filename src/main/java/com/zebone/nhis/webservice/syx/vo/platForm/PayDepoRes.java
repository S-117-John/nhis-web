package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="res")
@XmlAccessorType( XmlAccessType.FIELD)
public class PayDepoRes {
	@XmlElement(name="resultCode")
	private String resultCode;
	
	@XmlElement(name="resultDesc")
	private String resultDesc;
	
	@XmlElement(name="orderIdHis")
	private String orderIdHis;
	
	@XmlElement(name="orderDesc")
	private String orderDesc;
	
	@XmlElement(name="foregiftAmount")
	private String foregiftAmount;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public String getOrderIdHis() {
		return orderIdHis;
	}

	public void setOrderIdHis(String orderIdHis) {
		this.orderIdHis = orderIdHis;
	}

	public String getOrderDesc() {
		return orderDesc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}

	public String getForegiftAmount() {
		return foregiftAmount;
	}

	public void setForegiftAmount(String foregiftAmount) {
		this.foregiftAmount = foregiftAmount;
	}
	
	
}
