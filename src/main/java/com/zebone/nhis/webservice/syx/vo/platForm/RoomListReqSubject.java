package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class RoomListReqSubject extends PlatFormReq<RoomListReq> {

    @XmlElement(name="subject")
	@Override
	public List<RoomListReq> getSubject() {
		
		return super.subject;
	}

	@Override
	public void setSubject(List<RoomListReq> subject) {
		
		super.subject = subject;
	}

	
}
