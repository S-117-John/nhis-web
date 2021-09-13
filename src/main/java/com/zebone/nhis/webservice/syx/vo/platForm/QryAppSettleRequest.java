package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="request")
public class QryAppSettleRequest extends PlatFormReq<QryAppSettleSubject> {
	
	@XmlElement(name="subject")
	@Override
	public List<QryAppSettleSubject> getSubject() {
		// TODO Auto-generated method stub
		return super.subject;
	}
	
	@Override
	public void setSubject(List<QryAppSettleSubject> subject) {
		// TODO Auto-generated method stub
		super.subject=subject;
	}
	
}
