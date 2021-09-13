package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="request")
public class QryPiDeposRequest extends PlatFormReq<QryPiDeposReqSubject> {

	@XmlElement(name="subject")
	@Override
	public List<QryPiDeposReqSubject> getSubject() {
		// TODO Auto-generated method stub
		return super.subject;
	}

	@Override
	public void setSubject(List<QryPiDeposReqSubject> subject) {
		// TODO Auto-generated method stub
		super.subject=subject;
	}
	
}
