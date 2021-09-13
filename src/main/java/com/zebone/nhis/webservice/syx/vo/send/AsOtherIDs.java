package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("asOtherIDs")
public class AsOtherIDs {
	@XStreamAsAttribute
	private String classCode;
	private Id id = new Id();
	private ScopingOrganization scopingOrganization ;

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public ScopingOrganization getScopingOrganization() {
		if(scopingOrganization==null)scopingOrganization=new ScopingOrganization();
		return scopingOrganization;
	}

	public void setScopingOrganization(ScopingOrganization scopingOrganization) {
		this.scopingOrganization = scopingOrganization;
	}

}
