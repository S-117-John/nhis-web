package com.zebone.nhis.pro.zsba.msg.vo;

public class Refuse {
	private String doctor;
	private String nurse;
	private String group;
	private String dept;
	private String region;
	private String pvcode;
	public String getPvcode() {
		return pvcode;
	}
	public void setPvcode(String pvcode) {
		this.pvcode = pvcode;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getNurse() {
		return nurse;
	}
	public void setNurse(String nurse) {
		this.nurse = nurse;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}	
}
