package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="subject")
public class QryAppSettleResSubject {
	@XmlElement(name="res")
	private QryAppSettleResVo resVo;

	public QryAppSettleResVo getResVo() {
		return resVo;
	}

	public void setResVo(QryAppSettleResVo resVo) {
		this.resVo = resVo;
	}
	
	
}
