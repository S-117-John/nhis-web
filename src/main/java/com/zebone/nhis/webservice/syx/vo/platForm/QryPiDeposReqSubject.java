package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="subject")
public class QryPiDeposReqSubject {
	@XmlElement(name="req")
	private QryPiDeposReqVo deposReqVo;

	public QryPiDeposReqVo getDeposReqVo() {
		return deposReqVo;
	}

	public void setDeposReqVo(QryPiDeposReqVo deposReqVo) {
		this.deposReqVo = deposReqVo;
	}
	
	
}
