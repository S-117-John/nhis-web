package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="subject")
public class QryAppDictResSubject {
	@XmlElement(name="res")
	private QryAppDictResVo resVo;

	public QryAppDictResVo getResVo() {
		return resVo;
	}

	public void setResVo(QryAppDictResVo resVo) {
		this.resVo = resVo;
	}
	
	
}
