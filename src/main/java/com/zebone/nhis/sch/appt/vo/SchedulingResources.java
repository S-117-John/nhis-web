package com.zebone.nhis.sch.appt.vo;

import java.util.Date;

public class SchedulingResources {
	private Date lastDateTime;
	private String pkSchsrv;
	private String srvName;
	private String pkSchres;
	private String resName;
	private String pkEmp;
	private String nameEmp;
	private String photo;
	private String euRestype;
	private String nameDept;
	private String jobName;
	private String pkDept;
	private int pageNum;
    private int pageSize;
	public Date getLastDateTime() {
		return lastDateTime;
	}
	public void setLastDateTime(Date lastDateTime) {
		this.lastDateTime = lastDateTime;
	}
	public String getPkSchsrv() {
		return pkSchsrv;
	}
	public void setPkSchsrv(String pkSchsrv) {
		this.pkSchsrv = pkSchsrv;
	}
	public String getSrvName() {
		return srvName;
	}
	public void setSrvName(String srvName) {
		this.srvName = srvName;
	}
	public String getPkSchres() {
		return pkSchres;
	}
	public void setPkSchres(String pkSchres) {
		this.pkSchres = pkSchres;
	}
	public String getResName() {
		return resName;
	}
	public void setResName(String resName) {
		this.resName = resName;
	}
	public String getPkEmp() {
		return pkEmp;
	}
	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}
	public String getNameEmp() {
		return nameEmp;
	}
	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getEuRestype() {
		return euRestype;
	}
	public void setEuRestype(String euRestype) {
		this.euRestype = euRestype;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}
