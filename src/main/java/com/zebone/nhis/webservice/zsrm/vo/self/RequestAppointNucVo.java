package com.zebone.nhis.webservice.zsrm.vo.self;

public class RequestAppointNucVo extends  CommonReqSelfVo {

	private String resourceType;
	private String implicitRlues;
	private String patientId;
	private String appointMentDate;
	
	public String getResourceType() {
		return resourceType;
	}
	public String getImplicitRlues() {
		return implicitRlues;
	}
	public String getPatientId() {
		return patientId;
	}
	public String getAppointMentDate() {
		return appointMentDate;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public void setImplicitRlues(String implicitRlues) {
		this.implicitRlues = implicitRlues;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public void setAppointMentDate(String appointMentDate) {
		this.appointMentDate = appointMentDate;
	}
	

}
