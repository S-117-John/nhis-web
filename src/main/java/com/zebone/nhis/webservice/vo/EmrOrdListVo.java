package com.zebone.nhis.webservice.vo;

import java.sql.Timestamp;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;



/**
 * @author chengjia
 *
 */
public class EmrOrdListVo{

	private String pkPv;
	
    private String codeIp;

    private String name;
    
    private int ipTimes;
    
    private String codePv;
    
    private Integer ordsn;

    private Integer ordsnParent;
	
    private Integer groupno;
	
    private String nameOrd;
    
    private String spec;
    
    private Date dateStart;
    
    private Date dateStop;
    
    private Double dosage;
    
    private String dosageUnitName;
    
    private Double quan;
    
    private String unitName;
   
    private String freqName;
    
    private String flagDrug;
    
    private String noteOrd;
    
    private String nameEmpOrd;
    
    private String euAlways;
    
    private String supplyName;
    
    private String flagOp;
    
    private String flagDoctor;
    
	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIpTimes() {
		return ipTimes;
	}

	public void setIpTimes(int ipTimes) {
		this.ipTimes = ipTimes;
	}

	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}
	public Integer getOrdsn() {
		return ordsn;
	}

	public void setOrdsn(Integer ordsn) {
		this.ordsn = ordsn;
	}

	public Integer getOrdsnParent() {
		return ordsnParent;
	}

	public void setOrdsnParent(Integer ordsnParent) {
		this.ordsnParent = ordsnParent;
	}

	public Integer getGroupno() {
		return groupno;
	}

	public void setGroupno(Integer groupno) {
		this.groupno = groupno;
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

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateStop() {
		return dateStop;
	}

	public void setDateStop(Date dateStop) {
		this.dateStop = dateStop;
	}

	public Double getDosage() {
		return dosage;
	}

	public void setDosage(Double dosage) {
		this.dosage = dosage;
	}

	public String getDosageUnitName() {
		return dosageUnitName;
	}

	public void setDosageUnitName(String dosageUnitName) {
		this.dosageUnitName = dosageUnitName;
	}

	public Double getQuan() {
		return quan;
	}

	public void setQuan(Double quan) {
		this.quan = quan;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getFreqName() {
		return freqName;
	}

	public void setFreqName(String freqName) {
		this.freqName = freqName;
	}

	public String getFlagDrug() {
		return flagDrug;
	}

	public void setFlagDrug(String flagDrug) {
		this.flagDrug = flagDrug;
	}

	public String getNoteOrd() {
		return noteOrd;
	}

	public void setNoteOrd(String noteOrd) {
		this.noteOrd = noteOrd;
	}

	public String getNameEmpOrd() {
		return nameEmpOrd;
	}

	public void setNameEmpOrd(String nameEmpOrd) {
		this.nameEmpOrd = nameEmpOrd;
	}

	public String getEuAlways() {
		return euAlways;
	}

	public void setEuAlways(String euAlways) {
		this.euAlways = euAlways;
	}

	public String getSupplyName() {
		return supplyName;
	}

	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}

	public String getFlagOp() {
		return flagOp;
	}

	public void setFlagOp(String flagOp) {
		this.flagOp = flagOp;
	}

	public String getFlagDoctor() {
		return flagDoctor;
	}

	public void setFlagDoctor(String flagDoctor) {
		this.flagDoctor = flagDoctor;
	}
    
    
}