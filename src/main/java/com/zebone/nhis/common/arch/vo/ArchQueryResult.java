package com.zebone.nhis.common.arch.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "result")
public class ArchQueryResult {

	private Response response;

	private ArchResultBody archResultBody;
	
	@XmlElement(name="body")
	public ArchResultBody getArchResultBody() {
		return archResultBody;
	}

	

	public void setArchResultBody(ArchResultBody archResulrBody) {
		this.archResultBody = archResulrBody;
	}

	@XmlElement(name="response")
	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}
	
	
}
