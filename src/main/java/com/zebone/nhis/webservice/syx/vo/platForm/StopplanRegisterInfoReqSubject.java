package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class StopplanRegisterInfoReqSubject extends PlatFormReq<StopplanRegisterInfoReq> {

	@XmlElementWrapper(name="subject")
    @XmlElement(name="req")
	@Override
	public List<StopplanRegisterInfoReq> getSubject() {
		
		return super.subject;
	}

	@Override
	public void setSubject(List<StopplanRegisterInfoReq> subject) {
		
		super.subject = subject;
	}

	
}
