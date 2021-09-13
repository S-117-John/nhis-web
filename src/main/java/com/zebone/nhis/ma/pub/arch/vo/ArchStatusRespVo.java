package com.zebone.nhis.ma.pub.arch.vo;

public class ArchStatusRespVo {

	private ArchResponse response;
	
	private ArchStatusResultBodyVo body;
	
	public ArchResponse getResponse() {
		return response;
	}


	public ArchStatusResultBodyVo getBody() {
		return body;
	}


	public void setBody(ArchStatusResultBodyVo body) {
		this.body = body;
	}

	public void setResponse(ArchResponse response) {
		this.response = response;
	}
	
}
