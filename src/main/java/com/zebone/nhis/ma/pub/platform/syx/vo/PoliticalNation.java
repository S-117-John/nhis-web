package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("politicalNation")
public class PoliticalNation {
	
	private Code code;

	public Code getCode() {
		if(code == null)code = new Code();
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}
	
	
}
