package com.zebone.nhis.emr.mgr.vo;

import java.util.Date;

import com.zebone.nhis.common.module.emr.rec.dict.ViewEmrPatList;

public class EmrOpenEditRecParam extends ViewEmrPatList{

	private String pkEditRec;
	
	private String pkPv;
	
	private String pkPi;
	//住院次数
	private String times;
	//开发类型
	private String euType;
	//申请人
	private String pkEmpApply;
	//申请科室
	private String pkDateApply;
	//申请日期
	private Date ApplyDate;
	//申请期限
	private int timeLimit;
	//申请开始时间
	private Date openBeginDate;
	//申请截止时间
	private Date openEndDate;
	//申请理由
	private String applyTxt;
	//记录状态
	private String openEuStatus;
	//审批人
	private String pkEmpApprove;
	//审批科室
	private String pkDaptApprove;
	//审批时间
	private String approveDate;
	//审批意见
	private String approveTxt;
	public String getPkEditRec() {
		return pkEditRec;
	}
	public void setPkEditRec(String pkEditRec) {
		this.pkEditRec = pkEditRec;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	public String getEuType() {
		return euType;
	}
	public void setEuType(String euType) {
		this.euType = euType;
	}
	public String getPkEmpApply() {
		return pkEmpApply;
	}
	public void setPkEmpApply(String pkEmpApply) {
		this.pkEmpApply = pkEmpApply;
	}
	public String getPkDateApply() {
		return pkDateApply;
	}
	public void setPkDateApply(String pkDateApply) {
		this.pkDateApply = pkDateApply;
	}
	public Date getApplyDate() {
		return ApplyDate;
	}
	public void setApplyDate(Date applyDate) {
		ApplyDate = applyDate;
	}
	public int getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}
	public Date getOpenBeginDate() {
		return openBeginDate;
	}
	public void setOpenBeginDate(Date openBeginDate) {
		this.openBeginDate = openBeginDate;
	}
	public Date getOpenEndDate() {
		return openEndDate;
	}
	public void setOpenEndDate(Date openEndDate) {
		this.openEndDate = openEndDate;
	}
	public String getApplyTxt() {
		return applyTxt;
	}
	public void setApplyTxt(String applyTxt) {
		this.applyTxt = applyTxt;
	}
	public String getOpenEuStatus() {
		return openEuStatus;
	}
	public void setOpenEuStatus(String openEuStatus) {
		this.openEuStatus = openEuStatus;
	}
	public String getPkEmpApprove() {
		return pkEmpApprove;
	}
	public void setPkEmpApprove(String pkEmpApprove) {
		this.pkEmpApprove = pkEmpApprove;
	}
	public String getPkDaptApprove() {
		return pkDaptApprove;
	}
	public void setPkDaptApprove(String pkDaptApprove) {
		this.pkDaptApprove = pkDaptApprove;
	}
	public String getApproveDate() {
		return approveDate;
	}
	public void setApproveDate(String approveDate) {
		this.approveDate = approveDate;
	}
	public String getApproveTxt() {
		return approveTxt;
	}
	public void setApproveTxt(String approveTxt) {
		this.approveTxt = approveTxt;
	}
	
}
