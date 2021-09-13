package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("parameterList")
public class ParameterList {
	private Id id;
	private LivingSubjectAdministrativeGender livingSubjectAdministrativeGender;
	private LivingSubjectId livingSubjectId;
	private LivingSubjectName livingSubjectName;
	public Id getId() {
		if(id==null)id=new Id();
		return id;
	}
	public void setId(Id id) {
		this.id = id;
	}
	public LivingSubjectAdministrativeGender getLivingSubjectAdministrativeGender() {
		if(livingSubjectAdministrativeGender==null)livingSubjectAdministrativeGender=new LivingSubjectAdministrativeGender();
		return livingSubjectAdministrativeGender;
	}
	public void setLivingSubjectAdministrativeGender(
			LivingSubjectAdministrativeGender livingSubjectAdministrativeGender) {
		this.livingSubjectAdministrativeGender = livingSubjectAdministrativeGender;
	}
	public LivingSubjectId getLivingSubjectId() {
		if(livingSubjectId==null)livingSubjectId=new LivingSubjectId();
		return livingSubjectId;
	}
	public void setLivingSubjectId(LivingSubjectId livingSubjectId) {
		this.livingSubjectId = livingSubjectId;
	}
	public LivingSubjectName getLivingSubjectName() {
		if(livingSubjectName==null)livingSubjectName=new LivingSubjectName();
		return livingSubjectName;
	}
	public void setLivingSubjectName(LivingSubjectName livingSubjectName) {
		this.livingSubjectName = livingSubjectName;
	}
	
	
}
