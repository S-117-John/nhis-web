package com.zebone.platform.framework.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class SysResponseData {

	@XmlElement(name="RETURNCODE")
	private String returncode;
	
	@XmlElement(name="RETURNMSG")
	private String returnmsg;
	
	@XmlElement(name="RESPONSE")
	private  String rjson;

	public String getReturncode() {
		return returncode;
	}

	public void setReturncode(String returncode) {
		this.returncode = returncode;
	}

	public String getReturnmsg() {
		return returnmsg;
	}

	public void setReturnmsg(String returnmsg) {
		this.returnmsg = returnmsg;
	}

	public String getRjson() {
		return rjson;
	}

	public void setRjson(String rjson) {
		this.rjson = rjson;
	}
}
