package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="subject")
public class QryPiDeposResSubject {
	@XmlElement(name="res")
	private QryPiDeposResVo resVo;

	public QryPiDeposResVo getResVo() {
		return resVo;
	}

	public void setResVo(QryPiDeposResVo resVo) {
		this.resVo = resVo;
	}
	
	
}
