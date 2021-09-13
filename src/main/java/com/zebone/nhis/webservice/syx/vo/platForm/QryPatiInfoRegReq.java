package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="req")
public class QryPatiInfoRegReq  extends QryPatiInfoVo{
	@XmlElement(name = "codePi")
	private String codePi;
	
	@XmlElement(name = "height")
	private String height;
	
	@XmlElement(name = "weight")
	private String weight;
	
	@XmlElement(name = "dateBegin")
	private String dateBegin;
	
	@XmlElement(name = "pkInsu")
	private String pkInsu;
	
	@XmlElement(name = "nameInsu")
	private String nameInsu;
	
	@XmlElement(name = "dtPvsource")
	private String dtPvsource;
	
	@XmlElement(name = "nameRel")
	private String nameRel;
	
	@XmlElement(name = "codeDept")
	private String codeDept;
	
	@XmlElement(name = "nameDept")
	private String nameDept;
	
	@XmlElement(name="codeDeptNs")
	private String codeDeptNs;
	
	@XmlElement(name="codeDeptNs")
	private String nameDeptNs;
	
	@XmlElement(name = "codeEmpTre")
	private String codeEmpTre;
	
	@XmlElement(name = "dateReg")
	private String dateReg;
	
	@XmlElement(name = "pkIpNotice")
	private String pkIpNotice;
	
	@XmlElement(name = "dtLevelDise")
	private String dtLevelDise;
	
	@XmlElement(name = "codeDiag")
	private String codeDiag;
	
	@XmlElement(name = "descDiag")
	private String descDiag;
	
	@XmlElement(name = "note")
	private String note;
	
	@XmlElement(name="codeDeptNow")
	private String codeDeptNow;
	
	@XmlElement(name="codeEmpNow")
	private String codeEmpNow;
	
	@XmlElement(name="nameEmpNow")
	private String nameEmpNow;
	
	@XmlElement(name="pkPv")
	private String pkPv;
	
	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}

	public String getPkInsu() {
		return pkInsu;
	}

	public void setPkInsu(String pkInsu) {
		this.pkInsu = pkInsu;
	}

	public String getNameInsu() {
		return nameInsu;
	}

	public void setNameInsu(String nameInsu) {
		this.nameInsu = nameInsu;
	}

	public String getDtPvsource() {
		return dtPvsource;
	}

	public void setDtPvsource(String dtPvsource) {
		this.dtPvsource = dtPvsource;
	}

	public String getNameRel() {
		return nameRel;
	}

	public void setNameRel(String nameRel) {
		this.nameRel = nameRel;
	}

	public String getCodeDept() {
		return codeDept;
	}

	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	public String getCodeDeptNs() {
		return codeDeptNs;
	}

	public void setCodeDeptNs(String codeDeptNs) {
		this.codeDeptNs = codeDeptNs;
	}

	public String getNameDeptNs() {
		return nameDeptNs;
	}

	public void setNameDeptNs(String nameDeptNs) {
		this.nameDeptNs = nameDeptNs;
	}

	public String getCodeEmpTre() {
		return codeEmpTre;
	}

	public void setCodeEmpTre(String codeEmpTre) {
		this.codeEmpTre = codeEmpTre;
	}

	public String getDateReg() {
		return dateReg;
	}

	public void setDateReg(String dateReg) {
		this.dateReg = dateReg;
	}

	public String getPkIpNotice() {
		return pkIpNotice;
	}

	public void setPkIpNotice(String pkIpNotice) {
		this.pkIpNotice = pkIpNotice;
	}

	public String getDtLevelDise() {
		return dtLevelDise;
	}

	public void setDtLevelDise(String dtLevelDise) {
		this.dtLevelDise = dtLevelDise;
	}

	public String getCodeDiag() {
		return codeDiag;
	}

	public void setCodeDiag(String codeDiag) {
		this.codeDiag = codeDiag;
	}

	public String getDescDiag() {
		return descDiag;
	}

	public void setDescDiag(String descDiag) {
		this.descDiag = descDiag;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCodeDeptNow() {
		return codeDeptNow;
	}

	public void setCodeDeptNow(String codeDeptNow) {
		this.codeDeptNow = codeDeptNow;
	}

	public String getCodeEmpNow() {
		return codeEmpNow;
	}

	public void setCodeEmpNow(String codeEmpNow) {
		this.codeEmpNow = codeEmpNow;
	}

	public String getNameEmpNow() {
		return nameEmpNow;
	}

	public void setNameEmpNow(String nameEmpNow) {
		this.nameEmpNow = nameEmpNow;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	
	
}
