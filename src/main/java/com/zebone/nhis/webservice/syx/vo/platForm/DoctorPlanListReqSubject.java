package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class DoctorPlanListReqSubject extends PlatFormReq<DoctorPlanListReq> {

    @XmlElement(name="subject")
	@Override
	public List<DoctorPlanListReq> getSubject() {
		
		return super.subject;
	}

	@Override
	public void setSubject(List<DoctorPlanListReq> subject) {
		
		super.subject = subject;
	}

	
}
