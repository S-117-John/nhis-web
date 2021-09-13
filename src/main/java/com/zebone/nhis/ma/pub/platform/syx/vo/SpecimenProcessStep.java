package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("specimenProcessStep")
public class SpecimenProcessStep {
	@XStreamAsAttribute
	private String moodCode;
	@XStreamAsAttribute
	private String classCode;
	
	private EffectiveTime effectiveTime;
	private Subject subject;
	private Performer performer;
	
	
	public String getMoodCode() {
		return moodCode;
	}

	public void setMoodCode(String moodCode) {
		this.moodCode = moodCode;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public EffectiveTime getEffectiveTime() {
		if(effectiveTime == null) {
			effectiveTime = new EffectiveTime();
		}
		return effectiveTime;
	}

	public void setEffectiveTime(EffectiveTime effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public Performer getPerformer() {
		if(performer == null) {
			performer = new Performer();
		}
		return performer;
	}

	public void setPerformer(Performer performer) {
		this.performer = performer;
	}

	public Subject getSubject() {
		if(subject == null)subject =new Subject();
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	

}
