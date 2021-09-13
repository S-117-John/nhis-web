package com.zebone.nhis.webservice.vo.sdyy.iron;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="body")
@XmlAccessorType(XmlAccessType.FIELD)
public class IronReqBody {
	@XmlElement(name="OrderNumber")
	private String orderNumber;
	
	@XmlElement(name="VisitNumber")
	private String visitNumber;
	
	@XmlElement(name="WindowNumber")
	private String windowNumber;

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getVisitNumber() {
		return visitNumber;
	}

	public void setVisitNumber(String visitNumber) {
		this.visitNumber = visitNumber;
	}

	public String dowNumber() {
		return windowNumber;
	}

	public void setWindowNumber(String windowNumber) {
		this.windowNumber = windowNumber;
	}
}
