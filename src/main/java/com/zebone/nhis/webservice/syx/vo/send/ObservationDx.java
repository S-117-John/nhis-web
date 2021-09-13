package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("observationDx")
public class ObservationDx {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String moodCode;
	
	private Code code;
	private StatusCode statusCode;
	private EffectiveTime effectiveTime;
	private Value value=new Value();
	private Author author;
	
	
	
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
		if(code==null)code=new Code();
		return code;
	}
	public void setCode(Code code) {
		this.code = code;
	}
	public StatusCode getStatusCode() {
		if(statusCode==null)statusCode=new StatusCode();
		return statusCode;
	}
	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}
	public Value getValue() {
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
	public Author getAuthor() {
		if(author==null)author=new Author();
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	public EffectiveTime getEffectiveTime() {
		if(effectiveTime==null)effectiveTime= new EffectiveTime();
		return effectiveTime;
	}
	public void setEffectiveTime(EffectiveTime effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	
}
