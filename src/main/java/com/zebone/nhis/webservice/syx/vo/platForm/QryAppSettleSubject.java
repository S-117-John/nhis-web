package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="subject")
public class QryAppSettleSubject {
	@XmlElement(name="req")
	private QryAppSettleReqVo reqVo;

	public QryAppSettleReqVo getReqVo() {
		return reqVo;
	}

	public void setReqVo(QryAppSettleReqVo reqVo) {
		this.reqVo = reqVo;
	}
}
