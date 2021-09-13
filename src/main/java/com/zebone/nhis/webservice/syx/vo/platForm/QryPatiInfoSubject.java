package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="subject")
public class QryPatiInfoSubject {
	@XmlElement(name="res")
	private QryPatiInfoResVo infoResVo;

	public QryPatiInfoResVo getInfoResVo() {
		return infoResVo;
	}

	public void setInfoResVo(QryPatiInfoResVo infoResVo) {
		this.infoResVo = infoResVo;
	}
	
	
}
