package com.zebone.nhis.common.arch.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType( propOrder = { "code", "errormsg" })  
public class Response {
	
	@XmlElement
	private String code;
	
	@XmlElement
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

	public static Response getErrorInstance(String exception) {
		Response res = new Response();
		res.setCode("-1");
		res.setErrormsg(exception);
		return res;
	}

	public static Response getInstance() {
		Response res = new Response();
		res.setCode("0");
		res.setErrormsg("");
		return res;
	}
	
	

}
