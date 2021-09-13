package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="subject")
@XmlAccessorType(XmlAccessType.FIELD)
public class QryPiInfoForInSub {
	@XmlElement(name="req")
	private ReqPiInfo reqPiInfo;

	public ReqPiInfo getReqPiInfo() {
		return reqPiInfo;
	}

	public void setReqPiInfo(ReqPiInfo reqPiInfo) {
		this.reqPiInfo = reqPiInfo;
	}
	
	
}
