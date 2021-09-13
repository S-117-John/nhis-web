package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("observation")
public class Observation {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String moodCode;
	
	private Code code;
	
	private Value value;

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getMoodCode() {
		return moodCode;
	}

	public void setMoodCode(String moodCode) {
		this.moodCode = moodCode;
	}

	public Code getCode() {
		if(code==null) code=new Code();
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public Value getValue() {
		if(value==null) value=new Value();
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}
	
	
}
