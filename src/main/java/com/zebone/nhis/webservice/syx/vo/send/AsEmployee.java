package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("asEmployee")
public class AsEmployee {
	@XStreamAsAttribute
	private String classCode;
	private OccupationCode occupationCode ;
	private EmployerOrganization employerOrganization ;

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public OccupationCode getOccupationCode() {
		if(occupationCode==null)occupationCode=new OccupationCode();
		return occupationCode;
	}

	public void setOccupationCode(OccupationCode occupationCode) {
		this.occupationCode = occupationCode;
	}

	public EmployerOrganization getEmployerOrganization() {
		if(employerOrganization==null) employerOrganization=new EmployerOrganization();
		return employerOrganization;
	}

	public void setEmployerOrganization(
			EmployerOrganization employerOrganization) {
		this.employerOrganization = employerOrganization;
	}

}
