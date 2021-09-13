package com.zebone.nhis.ma.pub.arch.vo;

public class ArchResultVo {

	private ArchResponse response;
	
	private ArchResultBodyVo body;
	
	private String filecontent;
	
	public ArchResponse getResponse() {
		return response;
	}

	public ArchResultBodyVo getBody() {
		return body;
	}

	public void setBody(ArchResultBodyVo body) {
		this.body = body;
	}

	public void setResponse(ArchResponse response) {
		this.response = response;
	}

	public String getFilecontent() {
		return filecontent;
	}

	public void setFilecontent(String filecontent) {
		this.filecontent = filecontent;
	}
	
	
}
