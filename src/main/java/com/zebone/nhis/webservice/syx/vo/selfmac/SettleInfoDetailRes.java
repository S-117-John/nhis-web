package com.zebone.nhis.webservice.syx.vo.selfmac;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "res")
public class SettleInfoDetailRes {
	
    @XmlElement(name="resultCode")
	private String resultCode;
	
    @XmlElement(name="resultDesc")
	private String resultDesc;
	
    @XmlElement(name="detail")
	private SettleInfoDt detail;

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

	public SettleInfoDt getDetail() {
		return detail;
	}

	public void setDetail(SettleInfoDt detail) {
		this.detail = detail;
	}
    
    
}
