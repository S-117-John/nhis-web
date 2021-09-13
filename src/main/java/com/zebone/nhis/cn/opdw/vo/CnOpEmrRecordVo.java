package com.zebone.nhis.cn.opdw.vo;

import java.util.Date;

import com.zebone.nhis.common.module.cn.opdw.CnOpEmrRecord;
import com.zebone.nhis.common.module.cn.opdw.PvDocEx;

public class CnOpEmrRecordVo extends CnOpEmrRecord {
	 private String flagFirst;
	 private String dmissDiag;
	 private String nameOrg;
	 private String nameDept;
	 private Date dateClinic; //就诊日期

	 //人员
	 private String codePi;
	 private String namePi;
	 private String sex;
	 private String agePv;
	 private Date dateBegin;
	 private String nameEmpPhy;
	 private String descDiag;
	 
	 //医嘱
	 private String pressNo;
	 private String nameOrd;
	 private String dosage;
	 private String unit;
	 private String supply;
	 private String freq;
	 
	 private String days;
	 private String quan;
	 
	 private Double bmi;
	 
	 private PvDocEx pvDoc;
	public Date getDateClinic() {
		return dateClinic;
	}

	public void setDateClinic(Date dateClinic) {
		this.dateClinic = dateClinic;
	}

	public String getNameOrg() {
		return nameOrg;
	}

	public void setNameOrg(String nameOrg) {
		this.nameOrg = nameOrg;
	}

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	public String getDmissDiag() {
		return dmissDiag;
	}

	public void setDmissDiag(String dmissDiag) {
		this.dmissDiag = dmissDiag;
	}

	public String getFlagFirst() {
		return flagFirst;
	}

	public void setFlagFirst(String flagFirst) {
		this.flagFirst = flagFirst;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAgePv() {
		return agePv;
	}

	public void setAgePv(String agePv) {
		this.agePv = agePv;
	}

	public Date getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}

	public String getNameEmpPhy() {
		return nameEmpPhy;
	}

	public void setNameEmpPhy(String nameEmpPhy) {
		this.nameEmpPhy = nameEmpPhy;
	}

	public String getDescDiag() {
		return descDiag;
	}

	public void setDescDiag(String descDiag) {
		this.descDiag = descDiag;
	}

	public String getPressNo() {
		return pressNo;
	}

	public void setPressNo(String pressNo) {
		this.pressNo = pressNo;
	}

	public String getNameOrd() {
		return nameOrd;
	}

	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getSupply() {
		return supply;
	}

	public void setSupply(String supply) {
		this.supply = supply;
	}

	public String getFreq() {
		return freq;
	}

	public void setFreq(String freq) {
		this.freq = freq;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getQuan() {
		return quan;
	}

	public void setQuan(String quan) {
		this.quan = quan;
	}

	public Double getBmi() {
		return bmi;
	}

	public void setBmi(Double bmi) {
		this.bmi = bmi;
	}

	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	public PvDocEx getPvDoc() {
		return pvDoc;
	}

	public void setPvDoc(PvDocEx pvDoc) {
		this.pvDoc = pvDoc;
	}



}
