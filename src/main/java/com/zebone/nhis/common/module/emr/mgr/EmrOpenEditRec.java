package com.zebone.nhis.common.module.emr.mgr;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EMR_OPEN_EDIT_REC
 * @author 
 *
 */
@Table(value="EMR_OPEN_EDIT_REC")
public class EmrOpenEditRec extends BaseModule{
	
	@PK
	@Field(value="pk_edit_rec",id=KeyId.UUID)
	private String pkEditRec;
	
	@Field(value="PK_PV")
	private String pkPv;//就诊主键
	
	@Field(value="PK_PI")
	private String pkPi;//患者主键
	
	@Field(value="Times")
	private String times;//住院次数
	
	@Field(value="EU_TYPE")
	private String euType;//开放类型
	
	@Field(value="PK_EMP_APPLY")
	private String pkEmpApply;//申请人
	
	@Field(value="PK_DEPT_APPLY")
	private String pkDeptApply;//申请科室
	
	@Field(value="APPLY_DATE")
	private Date applyDate;//申请时间
	
	@Field(value="TIME_LIMIT")
	private int timeLimit;//申请期限
	
	@Field(value="BEGIN_DATE")
	private Date beginDate;//开始时间
	
	@Field(value="END_DATE")
	private Date endDate;//截止时间
	
	@Field(value="APPLY_TXT")
	private String applyTxt;//开放目的
	
	@Field(value="EU_STATUS")
	private String euStatus;//记录状态
	
	@Field(value="PK_EMP_APPROVE")
	private String pkEmpApprove;//审批人
	
	@Field(value="PK_DEPT_APPROVE")
	private String pkDeptApprove;//审批科室
	
	@Field(value="APPROVE_DATE")
	private Date approveDate;//审批时间
	
	@Field(value="APPROVE_TXT")
	private String approveTxt;//审批意见

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

	public String getPkDeptApply() {
		return pkDeptApply;
	}

	public void setPkDeptApply(String pkDeptApply) {
		this.pkDeptApply = pkDeptApply;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}


	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getApplyTxt() {
		return applyTxt;
	}

	public void setApplyTxt(String applyTxt) {
		this.applyTxt = applyTxt;
	}

	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

	public String getPkEmpApprove() {
		return pkEmpApprove;
	}

	public void setPkEmpApprove(String pkEmpApprove) {
		this.pkEmpApprove = pkEmpApprove;
	}

	public String getPkDeptApprove() {
		return pkDeptApprove;
	}

	public void setPkDeptApprove(String pkDeptApprove) {
		this.pkDeptApprove = pkDeptApprove;
	}

	public Date getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	public String getApproveTxt() {
		return approveTxt;
	}

	public void setApproveTxt(String approveTxt) {
		this.approveTxt = approveTxt;
	}
	
}
