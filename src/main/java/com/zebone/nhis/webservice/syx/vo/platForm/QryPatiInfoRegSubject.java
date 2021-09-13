package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="subject")
public class QryPatiInfoRegSubject {
	@XmlElement(name="req")
	private QryPatiInfoRegReq patiInfoReqReqVo;

	public QryPatiInfoRegReq getPatiInfoReqReqVo() {
		return patiInfoReqReqVo;
	}

	public void setPatiInfoReqReqVo(QryPatiInfoRegReq patiInfoReqReqVo) {
		this.patiInfoReqReqVo = patiInfoReqReqVo;
	} 
	
	
}
