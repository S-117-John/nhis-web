package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class PayDetailInfoReqSubject extends PlatFormReq<PayDetailInfoReq> {

	@XmlElementWrapper(name="subject")
    @XmlElement(name="req")
	@Override
	public List<PayDetailInfoReq> getSubject() {
		
		return super.subject;
	}

	@Override
	public void setSubject(List<PayDetailInfoReq> subject) {
		
		super.subject = subject;
	}

	
}
