package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="res")
public class QryPiCostResVo {
	@XmlElement(name="inpatientId")
	private String inpatientId;
	@XmlElement(name="totalAmount")
	private String totalAmount;
	@XmlElement(name="foregiftAmount")
	private String foregiftAmount;
	@XmlElement(name="unpaidAmount")
	private String unpaidAmount;
	public String getInpatientId() {
		return inpatientId;
	}
	public void setInpatientId(String inpatientId) {
		this.inpatientId = inpatientId;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getForegiftAmount() {
		return foregiftAmount;
	}
	public void setForegiftAmount(String foregiftAmount) {
		this.foregiftAmount = foregiftAmount;
	}
	public String getUnpaidAmount() {
		return unpaidAmount;
	}
	public void setUnpaidAmount(String unpaidAmount) {
		this.unpaidAmount = unpaidAmount;
	}
	
	
}
