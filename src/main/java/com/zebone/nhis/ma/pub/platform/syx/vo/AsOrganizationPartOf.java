package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("asOrganizationPartOf")
public class AsOrganizationPartOf {
	@XStreamAsAttribute
	private String classCode;
	private WholeOrganization wholeOrganization;
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public WholeOrganization getWholeOrganization() {
		if(wholeOrganization==null) wholeOrganization=new WholeOrganization();
		return wholeOrganization;
	}
	public void setWholeOrganization(WholeOrganization wholeOrganization) {
		this.wholeOrganization = wholeOrganization;
	}
}
