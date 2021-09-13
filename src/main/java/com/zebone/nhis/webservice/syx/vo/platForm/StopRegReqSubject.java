package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class StopRegReqSubject extends PlatFormReq<StopRegReq> {

	@XmlElementWrapper(name="subject")
    @XmlElement(name="req")
	@Override
	public List<StopRegReq> getSubject() {
		
		return super.subject;
	}

	@Override
	public void setSubject(List<StopRegReq> subject) {
		
		super.subject = subject;
	}

	
}
