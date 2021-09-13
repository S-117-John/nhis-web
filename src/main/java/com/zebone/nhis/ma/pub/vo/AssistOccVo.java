package com.zebone.nhis.ma.pub.vo;

import java.util.Date;

public class AssistOccVo {
	
    private String pkAssocc;

    private String pkCnord;

    private String pkPv;

    private String pkPi;

    /** DT_OCCTYPE - 参见注释：医技执行-医嘱类型 */
    private String dtOcctype;

    /** EU_PVTYPE - 1 门诊，2 急诊，3 住院，4 体检，5 家床 */
    private String euPvtype;

    /** CODE_OCC - 可用于执行单条形码 */
    private String codeOcc;

    private String pkDept;

    private String pkEmpOrd;

    private String nameEmpOrd;

    private Date dateOrd;

    private Date datePlan;

    private Date dateAppt;

    private Double quanOcc;

    private Integer timesOcc;

    private Integer timesTotal;

    private String pkOrgOcc;

    private String pkDeptOcc;

    private String flagOcc;

    private Date dateOcc;

    private String pkEmpOcc;

    private String nameEmpOcc;

    private String flagCanc;

    private Date dateCanc;

    private String pkEmpCanc;

    private String nameEmpCanc;

    private String pkExocc;

    private String infantNo;

    /** EU_STATUS - 0 申请，1执行，9 取消 */
    private String euStatus;

    private String note;

    private String flagPrt;

    private Date modityTime;
	
	private String flagRefund;
	
    private String pkAssoccdt;

	private String flagMaj;

	private String pkOrd;

    private String modifier;
    
    private String ordsn;
    
    private String ordsnParent;
    
	public String getOrdsn() {
		return ordsn;
	}

	public void setOrdsn(String ordsn) {
		this.ordsn = ordsn;
	}

	public String getOrdsnParent() {
		return ordsnParent;
	}

	public void setOrdsnParent(String ordsnParent) {
		this.ordsnParent = ordsnParent;
	}

	public String getPkAssoccdt() {
		return pkAssoccdt;
	}

	public void setPkAssoccdt(String pkAssoccdt) {
		this.pkAssoccdt = pkAssoccdt;
	}

	public String getFlagMaj() {
		return flagMaj;
	}

	public void setFlagMaj(String flagMaj) {
		this.flagMaj = flagMaj;
	}

	public String getPkOrd() {
		return pkOrd;
	}

	public void setPkOrd(String pkOrd) {
		this.pkOrd = pkOrd;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getPkAssocc() {
		return pkAssocc;
	}

	public void setPkAssocc(String pkAssocc) {
		this.pkAssocc = pkAssocc;
	}

	public String getPkCnord() {
		return pkCnord;
	}

	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
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

	public String getDtOcctype() {
		return dtOcctype;
	}

	public void setDtOcctype(String dtOcctype) {
		this.dtOcctype = dtOcctype;
	}

	public String getEuPvtype() {
		return euPvtype;
	}

	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}

	public String getCodeOcc() {
		return codeOcc;
	}

	public void setCodeOcc(String codeOcc) {
		this.codeOcc = codeOcc;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getPkEmpOrd() {
		return pkEmpOrd;
	}

	public void setPkEmpOrd(String pkEmpOrd) {
		this.pkEmpOrd = pkEmpOrd;
	}

	public String getNameEmpOrd() {
		return nameEmpOrd;
	}

	public void setNameEmpOrd(String nameEmpOrd) {
		this.nameEmpOrd = nameEmpOrd;
	}

	public Date getDateOrd() {
		return dateOrd;
	}

	public void setDateOrd(Date dateOrd) {
		this.dateOrd = dateOrd;
	}

	public Date getDatePlan() {
		return datePlan;
	}

	public void setDatePlan(Date datePlan) {
		this.datePlan = datePlan;
	}

	public Date getDateAppt() {
		return dateAppt;
	}

	public void setDateAppt(Date dateAppt) {
		this.dateAppt = dateAppt;
	}

	public Double getQuanOcc() {
		return quanOcc;
	}

	public void setQuanOcc(Double quanOcc) {
		this.quanOcc = quanOcc;
	}

	public Integer getTimesOcc() {
		return timesOcc;
	}

	public void setTimesOcc(Integer timesOcc) {
		this.timesOcc = timesOcc;
	}

	public Integer getTimesTotal() {
		return timesTotal;
	}

	public void setTimesTotal(Integer timesTotal) {
		this.timesTotal = timesTotal;
	}

	public String getPkOrgOcc() {
		return pkOrgOcc;
	}

	public void setPkOrgOcc(String pkOrgOcc) {
		this.pkOrgOcc = pkOrgOcc;
	}

	public String getPkDeptOcc() {
		return pkDeptOcc;
	}

	public void setPkDeptOcc(String pkDeptOcc) {
		this.pkDeptOcc = pkDeptOcc;
	}

	public String getFlagOcc() {
		return flagOcc;
	}

	public void setFlagOcc(String flagOcc) {
		this.flagOcc = flagOcc;
	}

	public Date getDateOcc() {
		return dateOcc;
	}

	public void setDateOcc(Date dateOcc) {
		this.dateOcc = dateOcc;
	}

	public String getPkEmpOcc() {
		return pkEmpOcc;
	}

	public void setPkEmpOcc(String pkEmpOcc) {
		this.pkEmpOcc = pkEmpOcc;
	}

	public String getNameEmpOcc() {
		return nameEmpOcc;
	}

	public void setNameEmpOcc(String nameEmpOcc) {
		this.nameEmpOcc = nameEmpOcc;
	}

	public String getFlagCanc() {
		return flagCanc;
	}

	public void setFlagCanc(String flagCanc) {
		this.flagCanc = flagCanc;
	}

	public Date getDateCanc() {
		return dateCanc;
	}

	public void setDateCanc(Date dateCanc) {
		this.dateCanc = dateCanc;
	}

	public String getPkEmpCanc() {
		return pkEmpCanc;
	}

	public void setPkEmpCanc(String pkEmpCanc) {
		this.pkEmpCanc = pkEmpCanc;
	}

	public String getNameEmpCanc() {
		return nameEmpCanc;
	}

	public void setNameEmpCanc(String nameEmpCanc) {
		this.nameEmpCanc = nameEmpCanc;
	}

	public String getPkExocc() {
		return pkExocc;
	}

	public void setPkExocc(String pkExocc) {
		this.pkExocc = pkExocc;
	}

	public String getInfantNo() {
		return infantNo;
	}

	public void setInfantNo(String infantNo) {
		this.infantNo = infantNo;
	}

	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getFlagPrt() {
		return flagPrt;
	}

	public void setFlagPrt(String flagPrt) {
		this.flagPrt = flagPrt;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}

	public String getFlagRefund() {
		return flagRefund;
	}

	public void setFlagRefund(String flagRefund) {
		this.flagRefund = flagRefund;
	}
	
}
