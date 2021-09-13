package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("queryMatchObservation")
public class QueryMatchObservation {

	
	private Code code;
	
	private Value value;
	
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String moodCode;
	
	public Code getCode() {
		if(code==null)code=new Code();
		return code;
	}
	public void setCode(Code code) {
		this.code = code;
	}
	public Value getValue() {
		if(value==null)value=new Value();
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
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
}
