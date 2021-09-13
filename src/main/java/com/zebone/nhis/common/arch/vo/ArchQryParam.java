package com.zebone.nhis.common.arch.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "request")
public class ArchQryParam {

	private ArchQryParamBody archQryParamBody;

	@XmlElement(name="body")
	public ArchQryParamBody getArchQryParamBody() {
		return archQryParamBody;
	}

	public void setArchQryParamBody(ArchQryParamBody archQryParamBody) {
		this.archQryParamBody = archQryParamBody;
	}
	
}
