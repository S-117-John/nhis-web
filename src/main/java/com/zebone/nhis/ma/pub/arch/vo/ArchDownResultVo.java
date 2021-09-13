package com.zebone.nhis.ma.pub.arch.vo;

public class ArchDownResultVo {

	private ArchResponse response;
	
	private ArchDownResultBodyVo body;
	
	public ArchResponse getResponse() {
		return response;
	}


	public ArchDownResultBodyVo getBody() {
		return body;
	}


	public void setBody(ArchDownResultBodyVo body) {
		this.body = body;
	}


	public void setResponse(ArchResponse response) {
		this.response = response;
	}

	
}
