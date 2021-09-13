package com.zebone.nhis.pro.zsba.msg.vo;

public class PacsVo {
	private String patientId;
	private String recordSn;
	private String itemName;
	private String name;
	private String inpatientNo;
	private String applyUnit;
	private String applyDoctor;
	private String reportDoctor;
	private String reportUnit;
	public String getApplyUnit() {
		return applyUnit;
	}
	public void setApplyUnit(String applyUnit) {
		this.applyUnit = applyUnit;
	}
	public String getApplyDoctor() {
		return applyDoctor;
	}
	public void setApplyDoctor(String applyDoctor) {
		this.applyDoctor = applyDoctor;
	}
	public String getReportDoctor() {
		return reportDoctor;
	}
	public void setReportDoctor(String reportDoctor) {
		this.reportDoctor = reportDoctor;
	}
	public String getReportUnit() {
		return reportUnit;
	}
	public void setReportUnit(String reportUnit) {
		this.reportUnit = reportUnit;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getRecordSn() {
		return recordSn;
	}
	public void setRecordSn(String recordSn) {
		this.recordSn = recordSn;
	}
}
