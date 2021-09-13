package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class PayOrderReqSubject extends PlatFormReq<PayOrder> {

	@XmlElementWrapper(name="subject")
    @XmlElement(name="req")
	@Override
	public List<PayOrder> getSubject() {
		return super.subject;
	}

	@Override
	public void setSubject(List<PayOrder> subject) {
		super.subject = subject;
	}

}
