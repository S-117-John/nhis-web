package com.zebone.nhis.ma.pub.arch.vo;

public class ArchStatusResultVo {

	private ArchResponse response;
	
	private ArchStatusResultBodyVo body;
	
	private String filecontent;
	
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

	public String getFilecontent() {
		return filecontent;
	}

	public void setFilecontent(String filecontent) {
		this.filecontent = filecontent;
	}
	
	
}
