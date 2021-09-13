package com.zebone.nhis.webservice.vo.sdyy.iron;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="head")
@XmlAccessorType(XmlAccessType.FIELD)
public class IronReqHead {
	@XmlElement(name="ApplyID")
	private String applyId;
	
	@XmlElement(name="Time")
	private Date time;

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
	
}
