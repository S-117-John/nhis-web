package com.zebone.nhis.common.arch.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "result")
public class ArchResult {

	
	private Response response;

	private ArchResultBody archResultBody;
	
	private String filecontent;
	
	
	
	
	@XmlElement(name="body")
	public ArchResultBody getArchResultBody() {
		return archResultBody;
	}

	@XmlElement(name="filecontent")
	public String getFilecontent() {
		return filecontent;
	}

	public void setFilecontent(String filecontent) {
		this.filecontent = filecontent;
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
