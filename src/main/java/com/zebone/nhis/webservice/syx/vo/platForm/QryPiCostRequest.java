package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="request")
@XmlAccessorType(XmlAccessType.FIELD)
public class QryPiCostRequest extends PlatFormReq<QrypiCostSubject> {

	@XmlElement(name="subject")
	@Override
	public List<QrypiCostSubject> getSubject() {
		return super.subject;
	}

	@Override
	public void setSubject(List<QrypiCostSubject> subject) {
		super.subject=subject;
	}

}
