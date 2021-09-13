package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class PayOrderStatusReqSubject extends PlatFormReq<PayOrderStatusReq> {

	@XmlElementWrapper(name="subject")
    @XmlElement(name="req")
	@Override
	public List<PayOrderStatusReq> getSubject() {
		
		return super.subject;
	}

	@Override
	public void setSubject(List<PayOrderStatusReq> subject) {
		
		super.subject = subject;
	}

	
}
