package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class RegRecordsTodayReqSubject extends PlatFormReq<RegRecordsTodayReq> {

	@XmlElementWrapper(name="subject")
    @XmlElement(name="req")
	@Override
	public List<RegRecordsTodayReq> getSubject() {
		
		return super.subject;
	}

	@Override
	public void setSubject(List<RegRecordsTodayReq> subject) {
		
		super.subject = subject;
	}

	
}
