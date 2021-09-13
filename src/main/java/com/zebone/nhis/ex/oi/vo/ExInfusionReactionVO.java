package com.zebone.nhis.ex.oi.vo;

import java.util.Date;

import com.zebone.nhis.common.module.ex.oi.ExInfusionReaction;

public class ExInfusionReactionVO extends ExInfusionReaction {
	private String dtSexName;
	private String dtSex;
	private String pkOrd;
	private String MedicinName;
	private String codePv;
	private String EuStateName;
	private String namePi;
	private Date startd;
	private Date endd;	
	private String dosage;
	private String pkFactory;
	private String batchNo;
	private String dosageDef;
	private String codeSupply;
	private String medcinePeroid;
	
	/// <summary>
    /// 获取或设置单位名称
    /// </summary>
    private String unitName;
	
	 /// <summary>
    /// 获取或设置成组标识
    /// </summary>
    private String regDtNo;
	
	public String getDtSexName() {
		return dtSexName;
	}
	public void setDtSexName(String dtSexName) {
		this.dtSexName = dtSexName;
	}
	public String getNamePi() {
		return namePi;
	}
	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
	public String getDtSex() {
		return dtSex;
	}
	public void setDtSex(String dtSex) {
		this.dtSex = dtSex;
	}
	public String getPkOrd() {
		return pkOrd;
	}
	public void setPkOrd(String pkOrd) {
		this.pkOrd = pkOrd;
	}
	public String getMedicinName() {
		return MedicinName;
	}
	public void setMedicinName(String medicinName) {
		MedicinName = medicinName;
	}
	public String getCodePv() {
		return codePv;
	}
	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}
	
	public String getEuStateName() {
		return EuStateName;
	}
	public void setEuStateName(String euStateName) {
		EuStateName = euStateName;
	}
	public Date getStartd() {
		return startd;
	}
	public void setStartd(Date startd) {
		this.startd = startd;
	}
	public Date getEndd() {
		return endd;
	}
	public void setEndd(Date endd) {
		this.endd = endd;
	}
	public String getDosage() {
		return dosage;
	}
	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	public String getPkFactory() {
		return pkFactory;
	}
	public void setPkFactory(String pkFactory) {
		this.pkFactory = pkFactory;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getDosageDef() {
		return dosageDef;
	}
	public void setDosageDef(String dosageDef) {
		this.dosageDef = dosageDef;
	}
	public String getCodeSupply() {
		return codeSupply;
	}
	public void setCodeSupply(String codeSupply) {
		this.codeSupply = codeSupply;
	}
	public String getMedcinePeroid() {
		return medcinePeroid;
	}
	public void setMedcinePeroid(String medcinePeroid) {
		this.medcinePeroid = medcinePeroid;
	}
	public String getRegDtNo() {
		return regDtNo;
	}
	public void setRegDtNo(String regDtNo) {
		this.regDtNo = regDtNo;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	

}
