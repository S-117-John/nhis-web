package com.zebone.nhis.common.module.emr.rec.rec;

import java.math.BigDecimal;
import java.util.Date;

public class EmrOpOrdList {

	/**
     * 
     */
    private String pkCnord;
    /**
     * 
     */
    private String pkOrg;
    /**
     * 
     */
    private String pkOrd;
    /**
     * 
     */
    private String pkPv;
    /**
     * 
     */
    private String codeOrd;
    /**
     * 
     */
    private String nameOrd;
    /**
     * 
     */
    private String spec;
    /**
     * 
     */
    private String supply;
    /**
     * 
     */
    private String freqCode;
    /**
     * 
     */
    private String freq;
    /**
     * 
     */
    private BigDecimal ordsn;
    /**
     * 
     */
    private BigDecimal ordsnParent;
    /**
     * 
     */
    private String orderFlag;
    /**
     * 
     */
    private String orderStatus;
    /**
     * 
     */
    private String noteOrd;
    /**
     * 
     */
    private Date beginDate;
    
    private Date endDate;
    
    /**
     * 
     */
    private String pkEmpOrd;
    /**
     * 
     */
    private String nameEmpOrd;
    
    private String codeOrdtype;

    private String pkUnitDos;
    private String dosage; 
    private String dosageUnit;
    
    private String delFlag;
 
    private String flagMedout;
    
    private String freqNameChn;
    private String nameUnitDos;
 
    private String flagDrug;
   
    private String flagFit;
    
    private String descFit;
    
    private String ordtypeName;
    
    private String groupno;

	public String getPkCnord() {
		return pkCnord;
	}

	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkOrd() {
		return pkOrd;
	}

	public void setPkOrd(String pkOrd) {
		this.pkOrd = pkOrd;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getCodeOrd() {
		return codeOrd;
	}

	public void setCodeOrd(String codeOrd) {
		this.codeOrd = codeOrd;
	}

	public String getNameOrd() {
		return nameOrd;
	}

	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getSupply() {
		return supply;
	}

	public void setSupply(String supply) {
		this.supply = supply;
	}

	public String getFreqCode() {
		return freqCode;
	}

	public void setFreqCode(String freqCode) {
		this.freqCode = freqCode;
	}

	public String getFreq() {
		return freq;
	}

	public void setFreq(String freq) {
		this.freq = freq;
	}

	public BigDecimal getOrdsn() {
		return ordsn;
	}

	public void setOrdsn(BigDecimal ordsn) {
		this.ordsn = ordsn;
	}

	public BigDecimal getOrdsnParent() {
		return ordsnParent;
	}

	public void setOrdsnParent(BigDecimal ordsnParent) {
		this.ordsnParent = ordsnParent;
	}

	public String getOrderFlag() {
		return orderFlag;
	}

	public void setOrderFlag(String orderFlag) {
		this.orderFlag = orderFlag;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getNoteOrd() {
		return noteOrd;
	}

	public void setNoteOrd(String noteOrd) {
		this.noteOrd = noteOrd;
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

	public String getCodeOrdtype() {
		return codeOrdtype;
	}

	public void setCodeOrdtype(String codeOrdtype) {
		this.codeOrdtype = codeOrdtype;
	}

	public String getPkUnitDos() {
		return pkUnitDos;
	}

	public void setPkUnitDos(String pkUnitDos) {
		this.pkUnitDos = pkUnitDos;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public String getDosageUnit() {
		return dosageUnit;
	}

	public void setDosageUnit(String dosageUnit) {
		this.dosageUnit = dosageUnit;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getFlagMedout() {
		return flagMedout;
	}

	public void setFlagMedout(String flagMedout) {
		this.flagMedout = flagMedout;
	}

	public String getFreqNameChn() {
		return freqNameChn;
	}

	public void setFreqNameChn(String freqNameChn) {
		this.freqNameChn = freqNameChn;
	}

	public String getNameUnitDos() {
		return nameUnitDos;
	}

	public void setNameUnitDos(String nameUnitDos) {
		this.nameUnitDos = nameUnitDos;
	}

	public String getFlagDrug() {
		return flagDrug;
	}

	public void setFlagDrug(String flagDrug) {
		this.flagDrug = flagDrug;
	}

	public String getFlagFit() {
		return flagFit;
	}

	public void setFlagFit(String flagFit) {
		this.flagFit = flagFit;
	}

	public String getDescFit() {
		return descFit;
	}

	public void setDescFit(String descFit) {
		this.descFit = descFit;
	}

	public String getOrdtypeName() {
		return ordtypeName;
	}

	public void setOrdtypeName(String ordtypeName) {
		this.ordtypeName = ordtypeName;
	}

	public String getGroupno() {
		return groupno;
	}

	public void setGroupno(String groupno) {
		this.groupno = groupno;
	}
    
    
}
