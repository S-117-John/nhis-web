package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class PayOrderDetailReqSubject extends PlatFormReq<PayOrderDetailReq> {

	@XmlElementWrapper(name="subject")
    @XmlElement(name="req")
	@Override
	public List<PayOrderDetailReq> getSubject() {
		
		return super.subject;
	}

	@Override
	public void setSubject(List<PayOrderDetailReq> subject) {
		
		super.subject = subject;
	}

	
}
