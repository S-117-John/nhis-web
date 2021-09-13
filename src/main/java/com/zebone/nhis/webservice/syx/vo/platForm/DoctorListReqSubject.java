package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class DoctorListReqSubject extends PlatFormReq<DoctorListReq> {

    @XmlElement(name="subject")
	@Override
	public List<DoctorListReq> getSubject() {
		
		return super.subject;
	}

	@Override
	public void setSubject(List<DoctorListReq> subject) {
		
		super.subject = subject;
	}

	
}
