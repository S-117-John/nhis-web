package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class QueueListReqSubject extends PlatFormReq<QueueListReq> {

    @XmlElement(name="subject")
	@Override
	public List<QueueListReq> getSubject() {
		
		return super.subject;
	}

	@Override
	public void setSubject(List<QueueListReq> subject) {
		
		super.subject = subject;
	}

	
}
