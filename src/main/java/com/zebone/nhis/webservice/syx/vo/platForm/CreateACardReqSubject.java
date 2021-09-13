package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class CreateACardReqSubject extends PlatFormReq<CreateACardReq> {

	@XmlElementWrapper(name="subject")
    @XmlElement(name="req")
	@Override
	public List<CreateACardReq> getSubject() {
		
		return super.subject;
	}

	@Override
	public void setSubject(List<CreateACardReq> subject) {
		
		super.subject = subject;
	}

	
}
