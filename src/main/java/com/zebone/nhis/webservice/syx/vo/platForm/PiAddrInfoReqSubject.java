package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class PiAddrInfoReqSubject extends PlatFormReq<PiAddrInfo> {

	@XmlElementWrapper(name="subject")
    @XmlElement(name="req")
	@Override
	public List<PiAddrInfo> getSubject() {
		return super.subject;
	}

	@Override
	public void setSubject(List<PiAddrInfo> subject) {
		super.subject = subject;
	}

}
