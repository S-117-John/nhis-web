package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class RegLockReqSubject extends PlatFormReq<RegLockVo> {

	@XmlElementWrapper(name="subject")
    @XmlElement(name="req")
	@Override
	public List<RegLockVo> getSubject() {
		
		return super.subject;
	}

	@Override
	public void setSubject(List<RegLockVo> subject) {
		
		super.subject = subject;
	}

    
    
}
