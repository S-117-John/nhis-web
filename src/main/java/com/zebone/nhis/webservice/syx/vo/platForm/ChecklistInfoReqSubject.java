package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class ChecklistInfoReqSubject extends PlatFormReq<ChecklistInfoReq> {

	@XmlElementWrapper(name="subject")
    @XmlElement(name="req")
	@Override
	public List<ChecklistInfoReq> getSubject() {
		
		return super.subject;
	}

	@Override
	public void setSubject(List<ChecklistInfoReq> subject) {
		
		super.subject = subject;
	}

	
}
