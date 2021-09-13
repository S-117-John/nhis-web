package com.zebone.nhis.pro.zsrm.sch.vo;

import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslot;
import com.zebone.nhis.common.module.sch.plan.SchSch;

public class SchSchVo extends SchSch  {
	
	private String pkPlanweekCheck;
	
	private String pkSchplanCheck;

	private String schresName;
	
	private String schsrvName;
	
	private String pkTicketrules;
	
	private String flagTicket;
	
	private String weekNo;

	private String nameDateslot;
	
	private String nameDeptunit;
	
	private String codeDateslot;
	
	private String pkDeptRs;
	
	private String nameDeptRs;
	
	private String dtDateslottype;
	
	private String euRestype;

	private String pkEmp;

	/**医生姓名*/
	private String doctorName;

	private String cntApptSur;
	
	/**外部可预约数*/
	private Integer apptType;

	/**dtApptype<=1 可预约号*/
	private Integer cntApptIn;

	/**
	 * 对外可预约
	 */
	private Integer cntApptOut;

	/**
	 * 诊间已约
	 */
	private Integer CntApptCliniced;
	/**
	 * 现场已约
	 */
	private Integer CntApptLocaled;
	/**
	 * 对外已约
	 */
	private Integer CntApptOuted;

	private BdCodeDateslot bdCodeDateslot;

	public BdCodeDateslot getBdCodeDateslot() {
		return bdCodeDateslot;
	}

	public void setBdCodeDateslot(BdCodeDateslot bdCodeDateslot) {
		this.bdCodeDateslot = bdCodeDateslot;
	}

	public Integer getCntApptCliniced() {
		return CntApptCliniced;
	}

	public void setCntApptCliniced(Integer cntApptCliniced) {
		CntApptCliniced = cntApptCliniced;
	}

	public Integer getCntApptLocaled() {
		return CntApptLocaled;
	}

	public void setCntApptLocaled(Integer cntApptLocaled) {
		CntApptLocaled = cntApptLocaled;
	}

	public Integer getCntApptOuted() {
		return CntApptOuted;
	}

	public void setCntApptOuted(Integer cntApptOuted) {
		CntApptOuted = cntApptOuted;
	}

	public Integer getCntApptOut() {
		return cntApptOut;
	}

	public void setCntApptOut(Integer cntApptOut) {
		this.cntApptOut = cntApptOut;
	}

	public String getCntApptSur() {
		return cntApptSur;
	}

	public void setCntApptSur(String cntApptSur) {
		this.cntApptSur = cntApptSur;
	}

	public String getWeekNo() {
		return weekNo;
	}

	public void setWeekNo(String weekNo) {
		this.weekNo = weekNo;
	}

	public String getPkPlanweekCheck() {
		return pkPlanweekCheck;
	}

	public void setPkPlanweekCheck(String pkPlanweekCheck) {
		this.pkPlanweekCheck = pkPlanweekCheck;
	}

	public String getSchresName() {
		return schresName;
	}

	public void setSchresName(String schresName) {
		this.schresName = schresName;
	}

	public String getSchsrvName() {
		return schsrvName;
	}

	public void setSchsrvName(String schsrvName) {
		this.schsrvName = schsrvName;
	}

	public String getPkTicketrules() {
		return pkTicketrules;
	}

	public void setPkTicketrules(String pkTicketrules) {
		this.pkTicketrules = pkTicketrules;
	}

	public String getFlagTicket() {
		return flagTicket;
	}

	public void setFlagTicket(String flagTicket) {
		this.flagTicket = flagTicket;
	}

	public String getPkSchplanCheck() {
		return pkSchplanCheck;
	}

	public void setPkSchplanCheck(String pkSchplanCheck) {
		this.pkSchplanCheck = pkSchplanCheck;
	}

	public String getNameDateslot() {
		return nameDateslot;
	}

	public void setNameDateslot(String nameDateslot) {
		this.nameDateslot = nameDateslot;
	}

	public String getNameDeptunit() {
		return nameDeptunit;
	}

	public void setNameDeptunit(String nameDeptunit) {
		this.nameDeptunit = nameDeptunit;
	}

	public String getCodeDateslot() {
		return codeDateslot;
	}

	public void setCodeDateslot(String codeDateslot) {
		this.codeDateslot = codeDateslot;
	}

	public String getPkDeptRs() {
		return pkDeptRs;
	}

	public void setPkDeptRs(String pkDeptRs) {
		this.pkDeptRs = pkDeptRs;
	}

	public String getNameDeptRs() {
		return nameDeptRs;
	}

	public void setNameDeptRs(String nameDeptRs) {
		this.nameDeptRs = nameDeptRs;
	}

	public String getDtDateslottype() {
		return dtDateslottype;
	}

	public void setDtDateslottype(String dtDateslottype) {
		this.dtDateslottype = dtDateslottype;
	}

	public String getEuRestype() {
		return euRestype;
	}

	public void setEuRestype(String euRestype) {
		this.euRestype = euRestype;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}

	public Integer getApptType() {
		return apptType;
	}

	public void setApptType(Integer apptType) {
		this.apptType = apptType;
	}

	public Integer getCntApptIn() {
		return cntApptIn;
	}

	public void setCntApptIn(Integer cntApptIn) {
		this.cntApptIn = cntApptIn;
	}
}
