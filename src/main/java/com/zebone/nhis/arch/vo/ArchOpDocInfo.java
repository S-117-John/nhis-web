package com.zebone.nhis.arch.vo;

import java.util.Date;

public class ArchOpDocInfo {

	public String patiCode ;
    //患者姓名
    public String patiName ;
    //年龄
    public String age ;
    //性别
    public String sex ;
    //科室
    public String dept ;
    //次数
    public String ipTimes ;
    //文件类型
    public String fileType ;
    //文件名
    public String fileName ;
    //文件路径
    public String filePath ;
    //报告名称
    public String itemName ;
    //报告编号
    public String rid ;
    //报告日期
    public Date dateRpt ;
    //报告科室
    public String detpRpt ;
    //是否旧门诊(2为旧门诊)
    public String archType ;
	public String getPatiCode() {
		return patiCode;
	}
	public void setPatiCode(String patiCode) {
		this.patiCode = patiCode;
	}
	public String getPatiName() {
		return patiName;
	}
	public void setPatiName(String patiName) {
		this.patiName = patiName;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getIpTimes() {
		return ipTimes;
	}
	public void setIpTimes(String ipTimes) {
		this.ipTimes = ipTimes;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public Date getDateRpt() {
		return dateRpt;
	}
	public void setDateRpt(Date dateRpt) {
		this.dateRpt = dateRpt;
	}
	public String getDetpRpt() {
		return detpRpt;
	}
	public void setDetpRpt(String detpRpt) {
		this.detpRpt = detpRpt;
	}
	public String getArchType() {
		return archType;
	}
	public void setArchType(String archType) {
		this.archType = archType;
	}
    
    
}
