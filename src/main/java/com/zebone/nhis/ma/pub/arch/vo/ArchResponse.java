package com.zebone.nhis.ma.pub.arch.vo;

public class ArchResponse {
	

	private String code;
	

	private String errormsg;
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public static ArchResponse getErrorInstance(String exception) {
		ArchResponse res = new ArchResponse();
		res.setCode("-1");
		res.setErrormsg(exception);
		return res;
	}

	public static ArchResponse getInstance() {
		ArchResponse res = new ArchResponse();
		res.setCode("0");
		res.setErrormsg("");
		return res;
	}
	
	

}
