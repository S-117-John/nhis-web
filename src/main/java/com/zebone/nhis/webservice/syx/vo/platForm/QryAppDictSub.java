package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="subject")
public class QryAppDictSub {
	@XmlElement(name="req")
	private QryAppDictReq reqVo;

	public QryAppDictReq getReqVo() {
		return reqVo;
	}

	public void setReqVo(QryAppDictReq reqVo) {
		this.reqVo = reqVo;
	}
	
	
}
