package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="subject")
public class QryPatiInfoRegResSubject {
	@XmlElement(name="res")
	private QryPatiInfoRegRes patiInfoRegRes;

	public QryPatiInfoRegRes getPatiInfoRegRes() {
		return patiInfoRegRes;
	}

	public void setPatiInfoRegRes(QryPatiInfoRegRes patiInfoRegRes) {
		this.patiInfoRegRes = patiInfoRegRes;
	}
	
	
}
