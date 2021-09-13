package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("statusCode")
public class StatusCode {
	@XStreamAsAttribute
	private String code;
	private EffectiveTime effectiveTime;
	private PatientPerson patientPerson;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public EffectiveTime getEffectiveTime() {
		if(effectiveTime==null)effectiveTime=new EffectiveTime();
		return effectiveTime;
	}

	public void setEffectiveTime(EffectiveTime effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public PatientPerson getPatientPerson() {
		if(patientPerson==null)patientPerson=new PatientPerson();
		return patientPerson;
	}

	public void setPatientPerson(PatientPerson patientPerson) {
		this.patientPerson = patientPerson;
	}
	
}
