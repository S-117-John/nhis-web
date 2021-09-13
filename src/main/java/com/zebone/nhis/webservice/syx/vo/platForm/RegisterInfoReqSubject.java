package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class RegisterInfoReqSubject extends PlatFormReq<RegisterInfoReq> {

	@XmlElementWrapper(name="subject")
    @XmlElement(name="req")
	@Override
	public List<RegisterInfoReq> getSubject() {
		
		return super.subject;
	}

	@Override
	public void setSubject(List<RegisterInfoReq> subject) {
		
		super.subject = subject;
	}
}
