package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("encounter")
public class Encounter {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String moodCode;
	private Id id;
	private Code code;
	private StatusCode statusCode;
	private EffectiveTime effectiveTime;
	private Subject subject;
	private Location location;
	private PertinentInformation1 pertinentInformation1;
	
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
	public Id getId() {
		if(id==null)id=new Id();
		return id;
	}
	public void setId(Id id) {
		this.id = id;
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
	public EffectiveTime getEffectiveTime() {
		if(effectiveTime==null)effectiveTime=new EffectiveTime();
		return effectiveTime;
	}
	public void setEffectiveTime(EffectiveTime effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	public Subject getSubject() {
		if(subject==null)subject=new Subject();
		return subject;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	public Location getLocation() {
		if(location==null)location=new Location();
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public PertinentInformation1 getPertinentInformation1() {
		if(pertinentInformation1==null)pertinentInformation1=new PertinentInformation1();
		return pertinentInformation1;
	}
	public void setPertinentInformation1(PertinentInformation1 pertinentInformation1) {
		this.pertinentInformation1 = pertinentInformation1;
	}
	
}
