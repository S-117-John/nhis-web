package com.zebone.nhis.ma.pub.syx.vo;

import java.util.Date;
import java.util.List;

public class OrderOccVo {
    private String pkExocc;
    private Date datePlan;
    private String bedNo;
    private String namePi;
    private String nameOrd;
    private String quanOcc;
    private String nameFreq;
    private String nameSupply;
    private String dateStop;
    private String nameEmpCanc;
    private String dateCanc;
    private String pkPdapdt;
    private String nameUnit;
    private String pkUnit;
    private String pkOrgExec;
    private String pkDeptExec;
    private String flagPivas;
    private List<MedicineVo> medicineVoList;
	
    private Date datePlanDis;
    private String ordsnParent;
    private String ordsn;
    private String sign;
    private String pkCnord;
    
	public String getPkUnit() {
		return pkUnit;
	}
	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}
	public String getPkExocc() {
		return pkExocc;
	}
	public void setPkExocc(String pkExocc) {
		this.pkExocc = pkExocc;
	}
	public Date getDatePlan() {
		return datePlan;
	}
	public void setDatePlan(Date datePlan) {
		this.datePlan = datePlan;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getNamePi() {
		return namePi;
	}
	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
	public String getNameOrd() {
		return nameOrd;
	}
	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}
	public String getQuanOcc() {
		return quanOcc;
	}
	public void setQuanOcc(String quanOcc) {
		this.quanOcc = quanOcc;
	}
	public String getNameFreq() {
		return nameFreq;
	}
	public void setNameFreq(String nameFreq) {
		this.nameFreq = nameFreq;
	}
	public String getNameSupply() {
		return nameSupply;
	}
	public void setNameSupply(String nameSupply) {
		this.nameSupply = nameSupply;
	}
	public String getDateStop() {
		return dateStop;
	}
	public void setDateStop(String dateStop) {
		this.dateStop = dateStop;
	}
	public String getNameEmpCanc() {
		return nameEmpCanc;
	}
	public void setNameEmpCanc(String nameEmpCanc) {
		this.nameEmpCanc = nameEmpCanc;
	}
	public String getDateCanc() {
		return dateCanc;
	}
	public void setDateCanc(String dateCanc) {
		this.dateCanc = dateCanc;
	}
	public String getPkPdapdt() {
		return pkPdapdt;
	}
	public void setPkPdapdt(String pkPdapdt) {
		this.pkPdapdt = pkPdapdt;
	}
	public String getNameUnit() {
		return nameUnit;
	}
	public void setNameUnit(String nameUnit) {
		this.nameUnit = nameUnit;
	}
	public String getPkOrgExec() {
		return pkOrgExec;
	}
	public void setPkOrgExec(String pkOrgExec) {
		this.pkOrgExec = pkOrgExec;
	}
	public String getPkDeptExec() {
		return pkDeptExec;
	}
	public void setPkDeptExec(String pkDeptExec) {
		this.pkDeptExec = pkDeptExec;
	}
	public List<MedicineVo> getMedicineVoList() {
		return medicineVoList;
	}
	public void setMedicineVoList(List<MedicineVo> medicineVoList) {
		this.medicineVoList = medicineVoList;
	}
	public String getOrdsnParent() {
		return ordsnParent;
	}
	public void setOrdsnParent(String ordsnParent) {
		this.ordsnParent = ordsnParent;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getFlagPivas() {
		return flagPivas;
	}
	public void setFlagPivas(String flagPivas) {
		this.flagPivas = flagPivas;
	}
	public Date getDatePlanDis() {
		return datePlanDis;
	}
	public void setDatePlanDis(Date datePlanDis) {
		this.datePlanDis = datePlanDis;
	}
	public String getOrdsn() {
		return ordsn;
	}
	public void setOrdsn(String ordsn) {
		this.ordsn = ordsn;
	}
	public String getPkCnord() {
		return pkCnord;
	}
	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}
    
    
}
