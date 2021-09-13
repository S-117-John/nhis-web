package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="request")
@XmlAccessorType(XmlAccessType.FIELD)
public class PayDepoRequest extends PlatFormReq<PayDepoSubject> {

	@XmlElement(name="subject")
	@Override
	public List<PayDepoSubject> getSubject() {
		return super.subject;
	}

	@Override
	public void setSubject(List<PayDepoSubject> subject) {
		super.subject=subject;
	}

}
