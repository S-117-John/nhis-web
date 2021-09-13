package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("assignedPrincipalOrganization")
public class AssignedPrincipalOrganization {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String determinerCode;
	private Name name;
	private AsAffiliate asAffiliate;
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getDeterminerCode() {
		return determinerCode;
	}
	public void setDeterminerCode(String determinerCode) {
		this.determinerCode = determinerCode;
	}
	public Name getName() {
		if(name==null)name=new Name();
		return name;
	}
	public void setName(Name name) {
		this.name = name;
	}
	public AsAffiliate getAsAffiliate() {
		if(asAffiliate==null) asAffiliate=new AsAffiliate();
		return asAffiliate;
	}
	public void setAsAffiliate(AsAffiliate asAffiliate) {
		this.asAffiliate = asAffiliate;
	}
	
	
}
