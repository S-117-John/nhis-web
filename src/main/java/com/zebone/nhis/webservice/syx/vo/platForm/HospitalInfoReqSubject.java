package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class HospitalInfoReqSubject extends PlatFormReq<HospitalInfo> {

	@XmlElementWrapper(name="subject")
    @XmlElement(name="req")
	@Override
	public List<HospitalInfo> getSubject() {
		
		return super.subject;
	}

	@Override
	public void setSubject(List<HospitalInfo> subject) {
		
		super.subject = subject;
	}

    
    
}
