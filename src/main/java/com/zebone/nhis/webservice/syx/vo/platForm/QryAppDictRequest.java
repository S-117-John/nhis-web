package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="request")
@XmlAccessorType(XmlAccessType.NONE)
public class QryAppDictRequest  extends PlatFormReq<QryAppDictSub>{

	@XmlElement(name="subject")
	@Override
	public List<QryAppDictSub> getSubject() {
		// TODO Auto-generated method stub
		return super.subject;
	}

	@Override
	public void setSubject(List<QryAppDictSub> subject) {
		// TODO Auto-generated method stub
		super.subject=subject;
	}

}
