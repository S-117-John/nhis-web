package com.zebone.nhis.common.arch.vo;

import java.util.Date;

public class ArchLogPushVo {
	
	private String pkLog;// 日志主键
	
	private String patientId;// 患者ID

	private String patientName;// 患者姓名

	private String itemName;// 项目名称

	private Date dateUpload;// 上传时间

	private String filePath;// 文件地址

	private String fileName;// 文件名称

	private String applyDept;// 申请科室

	private String docType;// 检验|检查

	private String sex;// 性别

	private String age;// 年龄

	private String flag;// 来源标志

	private String flagPush;// 推送标志

	public String getPkLog() {
		return pkLog;
	}

	public void setPkLog(String pkLog) {
		this.pkLog = pkLog;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Date getDateUpload() {
		return dateUpload;
	}

	public void setDateUpload(Date dateUpload) {
		this.dateUpload = dateUpload;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getApplyDept() {
		return applyDept;
	}

	public void setApplyDept(String applyDept) {
		this.applyDept = applyDept;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getFlagPush() {
		return flagPush;
	}

	public void setFlagPush(String flagPush) {
		this.flagPush = flagPush;
	}
}
