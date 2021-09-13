package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class QryPiInfoForInReqVo extends PlatFormReq<QryPiInfoForInSub>{

	@XmlElement(name="subject")
	@Override
	public List<QryPiInfoForInSub> getSubject() {
		// TODO Auto-generated method stub
		return super.subject;
	}

	@Override
	public void setSubject(List<QryPiInfoForInSub> subject) {
		super.subject=subject;
		
	}

}
