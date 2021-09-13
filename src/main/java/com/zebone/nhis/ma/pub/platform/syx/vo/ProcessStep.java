package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("processStep")
public class ProcessStep {
	
	@XStreamAsAttribute
	private String classCode;

	private Code code;

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public Code getCode() {
		if(code == null) {
			code = new Code();
		}
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}
	
	
}
