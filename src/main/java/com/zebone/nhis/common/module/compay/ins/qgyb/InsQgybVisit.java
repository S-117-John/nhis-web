package com.zebone.nhis.common.module.compay.ins.qgyb;

import java.util.Date;

import org.joda.time.DateTime;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value = "ins_qgyb_visit")
public class InsQgybVisit extends BaseModule{

	@PK
	@Field(value = "pk_visit", id = KeyId.UUID)
	private String pkVisit;
	@Field(value = "pk_org")
	private String pkOrg;
	@Field(value = "pk_hp")
	private String pkHp;
	@Field(value = "pk_pv")
	private String pkPv;
	@Field(value = "pk_pi")
	private String pkPi;
	@Field(value = "IDNO")
	private String idno;

	/** 医保返回信息*/
	@Field(value = "mdtrt_id")
	private String mdtrtId;
	@Field(value = "psn_no")
	private String psnNo;
	
	@Field(value = "insutype")
	private String insutype;
	@Field(value = "mdtrt_cert_type")
	private String mdtrtCertType;
	@Field(value = "mdtrt_cert_no")
	private String mdtrtCertNo;
	
	/** 医保使用字段*/
	@Field(value = "flag_insu")
	private String flagInsu;
	@Field(value = "med_type")
	private String medType;
	
	@Field(value = "name_org")
	private String nameOrg;
	@Field(value = "name_pi")
	private String namePi;
	@Field(value = "code_center")
	private String codeCenter;
	@Field(value = "code_org")
	private String codeOrg;
	@Field(value = "note")
	private String note;
	@Field(value = "begntime")
	private Date begntime;

	@Field(value = "DT_IDTYPE")
	private String dtIdtype;

	@Field(value = "PERSONTYPE")
	private String persontype;

	@Field(value = "DATE_REG")
	private Date datereg;

	@Field(value = "CODE_INSST")
	private String codeInsst;

	public String getCodeInsst() {
		return codeInsst;
	}

	public void setCodeInsst(String codeInsst) {
		this.codeInsst = codeInsst;
	}

	public String getDtIdtype() {
		return dtIdtype;
	}

	public void setDtIdtype(String dtIdtype) {
		this.dtIdtype = dtIdtype;
	}

	public String getPersontype() {
		return persontype;
	}

	public void setPersontype(String persontype) {
		this.persontype = persontype;
	}

	public Date getDatereg() {
		return datereg;
	}

	public void setDatereg(Date datereg) {
		this.datereg = datereg;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getMedType() {
		return medType;
	}
	public void setMedType(String medType) {
		this.medType = medType;
	}
	public String getFlagInsu() {
		return flagInsu;
	}
	public void setFlagInsu(String flagInsu) {
		this.flagInsu = flagInsu;
	}
	
	public String getInsutype() {
		return insutype;
	}
	public void setInsutype(String insutype) {
		this.insutype = insutype;
	}
	public String getMdtrtCertType() {
		return mdtrtCertType;
	}
	public void setMdtrtCertType(String mdtrtCertType) {
		this.mdtrtCertType = mdtrtCertType;
	}
	public String getMdtrtCertNo() {
		return mdtrtCertNo;
	}
	public void setMdtrtCertNo(String mdtrtCertNo) {
		this.mdtrtCertNo = mdtrtCertNo;
	}
	public String getPsnNo() {
		return psnNo;
	}
	public void setPsnNo(String psnNo) {
		this.psnNo = psnNo;
	}
	public String getPkVisit() {
		return pkVisit;
	}
	public void setPkVisit(String pkVisit) {
		this.pkVisit = pkVisit;
	}
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public String getPkHp() {
		return pkHp;
	}
	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
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
	public String getMdtrtId() {
		return mdtrtId;
	}
	public void setMdtrtId(String mdtrtId) {
		this.mdtrtId = mdtrtId;
	}
	public String getNameOrg() {
		return nameOrg;
	}
	public void setNameOrg(String nameOrg) {
		this.nameOrg = nameOrg;
	}
	public String getNamePi() {
		return namePi;
	}
	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
	public String getCodeCenter() {
		return codeCenter;
	}
	public void setCodeCenter(String codeCenter) {
		this.codeCenter = codeCenter;
	}
	public String getCodeOrg() {
		return codeOrg;
	}
	public void setCodeOrg(String codeOrg) {
		this.codeOrg = codeOrg;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getBegntime() {
		return begntime;
	}
	public void setBegntime(Date begntime) {
		this.begntime = begntime;
	}
	
}
