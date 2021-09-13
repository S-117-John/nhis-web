package com.zebone.nhis.emr.rec.dict.vo;

import java.util.Date;

import com.zebone.nhis.common.module.emr.rec.dict.ViewEmrPatList;

public class EmrPatListPrarm extends ViewEmrPatList{
	
	private String qcName;
	
	private String finishName;
	
	private Date finishDate;
	
	private String submitName;
	
	private Date submitDate;
	
	private String empQcName;
	
	private Date empQcDate;
	
	private String euEmpQcGrade;
	
	private Double empQcScore;
	
	private Date deptQcDate;
	
	private String euDeptQcGrade;
	
	private Double deptQcScore;
	
	private String archiveName;
	
	private Date archiveDate;
	
	private String deptQcName;
	
	private String finalQcName;
	
	private String euFinalQcGrade;
	 
	private Double finalQcScore;
	
	private Date finalQcDate;
	/*签收标志*/
	private String flagReceive;
	/*签收日期*/
	private Date receiveDate;
	
	private String pkEmpReceive;
	
	/*编码标志*/
	private String flagCode;
	/*编码人*/
	private String pkEmpCode;
	/*编码日期*/
	private Date codeDate;
	private String euLinkQcGrade;
	private String pkEmpLinkQc;
	private String linkQcScore;
	private Date linkQcDate;
	private String empLinkQcName;
	private String EmpCodeName;
	private String flagAmendDept;
	private String flagAmendFinal;
	private String pkEmpFinalQcSubmit;
	private Date finalQcSubmitDate;
	private String pkEmpEmrArchive;
	private Date emrArchiveDate;
	private String insuName;//医保名称
	private String remark;//病历评分记录中的备注
	private String amountSt;//患者总费用
	private String amountInsu;//患者总费用

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFlagCode() {
		return flagCode;
	}

	public void setFlagCode(String flagCode) {
		this.flagCode = flagCode;
	}

	public String getPkEmpCode() {
		return pkEmpCode;
	}

	public void setPkEmpCode(String pkEmpCode) {
		this.pkEmpCode = pkEmpCode;
	}

	public String getFlagReceive() {
		return flagReceive;
	}

	public void setFlagReceive(String flagReceive) {
		this.flagReceive = flagReceive;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public Date getFinalQcDate() {
		return finalQcDate;
	}

	public void setFinalQcDate(Date finalQcDate) {
		this.finalQcDate = finalQcDate;
	}

	public String getQcName() {
		return qcName;
	}

	public void setQcName(String qcName) {
		this.qcName = qcName;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public String getFinishName() {
		return finishName;
	}

	public void setFinishName(String finishName) {
		this.finishName = finishName;
	}

	public String getSubmitName() {
		return submitName;
	}

	public void setSubmitName(String submitName) {
		this.submitName = submitName;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public String getEmpQcName() {
		return empQcName;
	}

	public void setEmpQcName(String empQcName) {
		this.empQcName = empQcName;
	}

	public Date getEmpQcDate() {
		return empQcDate;
	}

	public void setEmpQcDate(Date empQcDate) {
		this.empQcDate = empQcDate;
	}

	public String getEuEmpQcGrade() {
		return euEmpQcGrade;
	}

	public void setEuEmpQcGrade(String euEmpQcGrade) {
		this.euEmpQcGrade = euEmpQcGrade;
	}

	public Double getEmpQcScore() {
		return empQcScore;
	}

	public void setEmpQcScore(Double empQcScore) {
		this.empQcScore = empQcScore;
	}

	public Date getDeptQcDate() {
		return deptQcDate;
	}

	public void setDeptQcDate(Date deptQcDate) {
		this.deptQcDate = deptQcDate;
	}

	public String getEuDeptQcGrade() {
		return euDeptQcGrade;
	}

	public void setEuDeptQcGrade(String euDeptQcGrade) {
		this.euDeptQcGrade = euDeptQcGrade;
	}

	public Double getDeptQcScore() {
		return deptQcScore;
	}

	public void setDeptQcScore(Double deptQcScore) {
		this.deptQcScore = deptQcScore;
	}

	public String getArchiveName() {
		return archiveName;
	}

	public void setArchiveName(String archiveName) {
		this.archiveName = archiveName;
	}

	public Date getArchiveDate() {
		return archiveDate;
	}

	public void setArchiveDate(Date archiveDate) {
		this.archiveDate = archiveDate;
	}

	public String getDeptQcName() {
		return deptQcName;
	}

	public void setDeptQcName(String deptQcName) {
		this.deptQcName = deptQcName;
	}

	public String getFinalQcName() {
		return finalQcName;
	}

	public void setFinalQcName(String finalQcName) {
		this.finalQcName = finalQcName;
	}

	public String getEuFinalQcGrade() {
		return euFinalQcGrade;
	}

	public void setEuFinalQcGrade(String euFinalQcGrade) {
		this.euFinalQcGrade = euFinalQcGrade;
	}

	public Double getFinalQcScore() {
		return finalQcScore;
	}

	public void setFinalQcScore(Double finalQcScore) {
		this.finalQcScore = finalQcScore;
	}

	public String getPkEmpReceive() {
		return pkEmpReceive;
	}

	public void setPkEmpReceive(String pkEmpReceive) {
		this.pkEmpReceive = pkEmpReceive;
	}

	public Date getCodeDate() {
		return codeDate;
	}

	public void setCodeDate(Date codeDate) {
		this.codeDate = codeDate;
	}

	public String getEuLinkQcGrade() {
		return euLinkQcGrade;
	}

	public void setEuLinkQcGrade(String euLinkQcGrade) {
		this.euLinkQcGrade = euLinkQcGrade;
	}

	public String getPkEmpLinkQc() {
		return pkEmpLinkQc;
	}

	public void setPkEmpLinkQc(String pkEmpLinkQc) {
		this.pkEmpLinkQc = pkEmpLinkQc;
	}

	public String getLinkQcScore() {
		return linkQcScore;
	}

	public void setLinkQcScore(String linkQcScore) {
		this.linkQcScore = linkQcScore;
	}


	public Date getLinkQcDate() {
		return linkQcDate;
	}

	public void setLinkQcDate(Date linkQcDate) {
		this.linkQcDate = linkQcDate;
	}

	public String getEmpLinkQcName() {
		return empLinkQcName;
	}

	public void setEmpLinkQcName(String empLinkQcName) {
		this.empLinkQcName = empLinkQcName;
	}

	public String getEmpCodeName() {
		return EmpCodeName;
	}

	public void setEmpCodeName(String empCodeName) {
		EmpCodeName = empCodeName;
	}

	public String getFlagAmendDept() {
		return flagAmendDept;
	}

	public void setFlagAmendDept(String flagAmendDept) {
		this.flagAmendDept = flagAmendDept;
	}

	public String getFlagAmendFinal() {
		return flagAmendFinal;
	}

	public void setFlagAmendFinal(String flagAmendFinal) {
		this.flagAmendFinal = flagAmendFinal;
	}

	public String getPkEmpFinalQcSubmit() {
		return pkEmpFinalQcSubmit;
	}

	public void setPkEmpFinalQcSubmit(String pkEmpFinalQcSubmit) {
		this.pkEmpFinalQcSubmit = pkEmpFinalQcSubmit;
	}

	public Date getFinalQcSubmitDate() {
		return finalQcSubmitDate;
	}

	public void setFinalQcSubmitDate(Date finalQcSubmitDate) {
		this.finalQcSubmitDate = finalQcSubmitDate;
	}

	public String getPkEmpEmrArchive() {
		return pkEmpEmrArchive;
	}

	public void setPkEmpEmrArchive(String pkEmpEmrArchive) {
		this.pkEmpEmrArchive = pkEmpEmrArchive;
	}

	public Date getEmrArchiveDate() {
		return emrArchiveDate;
	}

	public void setEmrArchiveDate(Date emrArchiveDate) {
		this.emrArchiveDate = emrArchiveDate;
	}
	
	public String getInsuName() {
		return insuName;
	}

	public void setInsuName(String insuName) {
		this.insuName = insuName;
	}

	public String getAmountSt() {
		return amountSt;
	}

	public void setAmountSt(String amountSt) {
		this.amountSt = amountSt;
	}

	public String getAmountInsu() {
		return amountInsu;
	}

	public void setAmountInsu(String amountInsu) {
		this.amountInsu = amountInsu;
	}
	
}
