package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="patiInfo")
public class QryPatiInfoVo {

	@XmlElement(name = "pkOrg")
	private String pkOrg;
	
	@XmlElement(name = "pkPi")
	private String pkPi;
	
	@XmlElement(name = "namePi")
	private String namePi;
	
	@XmlElement(name = "dtSex")
	private String dtSex;
	
	@XmlElement(name = "agePv")
	private String agePv;
	
	@XmlElement(name = "birthDate")
	private String birthDate;
	
	@XmlElement(name = "dateAdmit")
	private Date dateAdmit;
	
	@XmlElement(name = "contactDept")
	private String contactDept;
	
	@XmlElement(name = "addrcodeCur")
	private String addrcodeCur;
	
	@XmlElement(name = "addrCur")
	private String addrCur;
	
	@XmlElement(name = "addrCurDt")
	private String addrCurDt;
	
	@XmlElement(name = "telNo")
	private String telNo;
	
	@XmlElement(name = "postcodeCur")
	private String postcodeCur;
	
	@XmlElement(name = "addrcodeRegi")
	private String addrcodeRegi;
	
	@XmlElement(name = "addrRegi")
	private String addrRegi;
	
	@XmlElement(name = "addrRegiDt")
	private String addrRegiDt;
	
	@XmlElement(name = "postcodeRegi")
	private String postcodeRegi;
	
	@XmlElement(name = "unitWork")
	private String unitWork;
	
	@XmlElement(name = "unitWork")
	private String telWork;
	
	@XmlElement(name = "postcodeWork")
	private String postcodeWork;
	
	@XmlElement(name = "dtIdtype")
	private String dtIdtype;
	
	@XmlElement(name = "idNo")
	private String idNo;
	
	@XmlElement(name = "dtRalation")
	private String dtRalation;
	
	@XmlElement(name = "telRel")
	private String telRel;
	
	@XmlElement(name = "addrRel")
	private String addrRel;
	
	@XmlElement(name = "codeIp")
	private String codeIp;
	
	@XmlElement(name = "dtMarry")
	private String dtMarry;
	
	@XmlElement(name = "dtCountry")
	private String dtCountry;
	
	@XmlElement(name = "dtNation")
	private String dtNation;
	
	@XmlElement(name = "mobile")
	private String mobile;
	
	@XmlElement(name = "dtOccu")
	private String dtOccu;
	
	@XmlElement(name = "addrcodeOrigin")
	private String addrcodeOrigin;
	
	@XmlElement(name = "addrOrigin")
	private String addrOrigin;
	
	@XmlElement(name = "insurNo")
	private String insurNo;
	
	@XmlElement(name="addrcodeBirth")
	private String addrcodeBirth;
	
	@XmlElement(name="addrBirth")
	private String addrBirth;
	
	@XmlElement(name="ipTimes")
	private Integer ipTimes;

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
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

	public String getAgePv() {
		return agePv;
	}

	public void setAgePv(String agePv) {
		this.agePv = agePv;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public Date getDateAdmit() {
		return dateAdmit;
	}

	public void setDateAdmit(Date dateAdmit) {
		this.dateAdmit = dateAdmit;
	}

	public String getContactDept() {
		return contactDept;
	}

	public void setContactDept(String contactDept) {
		this.contactDept = contactDept;
	}

	public String getAddrcodeCur() {
		return addrcodeCur;
	}

	public void setAddrcodeCur(String addrcodeCur) {
		this.addrcodeCur = addrcodeCur;
	}

	public String getAddrCur() {
		return addrCur;
	}

	public void setAddrCur(String addrCur) {
		this.addrCur = addrCur;
	}

	public String getAddrCurDt() {
		return addrCurDt;
	}

	public void setAddrCurDt(String addrCurDt) {
		this.addrCurDt = addrCurDt;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getPostcodeCur() {
		return postcodeCur;
	}

	public void setPostcodeCur(String postcodeCur) {
		this.postcodeCur = postcodeCur;
	}

	public String getAddrcodeRegi() {
		return addrcodeRegi;
	}

	public void setAddrcodeRegi(String addrcodeRegi) {
		this.addrcodeRegi = addrcodeRegi;
	}
	
	public String getAddrRegi() {
		return addrRegi;
	}

	public void setAddrRegi(String addrRegi) {
		this.addrRegi = addrRegi;
	}

	public String getAddrRegiDt() {
		return addrRegiDt;
	}

	public void setAddrRegiDt(String addrRegiDt) {
		this.addrRegiDt = addrRegiDt;
	}

	public String getPostcodeRegi() {
		return postcodeRegi;
	}

	public void setPostcodeRegi(String postcodeRegi) {
		this.postcodeRegi = postcodeRegi;
	}

	public String getUnitWork() {
		return unitWork;
	}

	public void setUnitWork(String unitWork) {
		this.unitWork = unitWork;
	}

	public String getTelWork() {
		return telWork;
	}

	public void setTelWork(String telWork) {
		this.telWork = telWork;
	}

	public String getPostcodeWork() {
		return postcodeWork;
	}

	public void setPostcodeWork(String postcodeWork) {
		this.postcodeWork = postcodeWork;
	}

	public String getDtIdtype() {
		return dtIdtype;
	}

	public void setDtIdtype(String dtIdtype) {
		this.dtIdtype = dtIdtype;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getDtRalation() {
		return dtRalation;
	}

	public void setDtRalation(String dtRalation) {
		this.dtRalation = dtRalation;
	}

	public String getTelRel() {
		return telRel;
	}

	public void setTelRel(String telRel) {
		this.telRel = telRel;
	}

	public String getAddrRel() {
		return addrRel;
	}

	public void setAddrRel(String addrRel) {
		this.addrRel = addrRel;
	}

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public String getDtMarry() {
		return dtMarry;
	}

	public void setDtMarry(String dtMarry) {
		this.dtMarry = dtMarry;
	}

	public String getDtCountry() {
		return dtCountry;
	}

	public void setDtCountry(String dtCountry) {
		this.dtCountry = dtCountry;
	}

	public String getDtNation() {
		return dtNation;
	}

	public void setDtNation(String dtNation) {
		this.dtNation = dtNation;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDtOccu() {
		return dtOccu;
	}

	public void setDtOccu(String dtOccu) {
		this.dtOccu = dtOccu;
	}

	public String getAddrcodeOrigin() {
		return addrcodeOrigin;
	}

	public void setAddrcodeOrigin(String addrcodeOrigin) {
		this.addrcodeOrigin = addrcodeOrigin;
	}

	public String getAddrOrigin() {
		return addrOrigin;
	}

	public void setAddrOrigin(String addrOrigin) {
		this.addrOrigin = addrOrigin;
	}

	public String getInsurNo() {
		return insurNo;
	}

	public void setInsurNo(String insurNo) {
		this.insurNo = insurNo;
	}

	public String getAddrcodeBirth() {
		return addrcodeBirth;
	}

	public void setAddrcodeBirth(String addrcodeBirth) {
		this.addrcodeBirth = addrcodeBirth;
	}

	public String getAddrBirth() {
		return addrBirth;
	}

	public void setAddrBirth(String addrBirth) {
		this.addrBirth = addrBirth;
	}

	public Integer getIpTimes() {
		return ipTimes;
	}

	public void setIpTimes(Integer ipTimes) {
		this.ipTimes = ipTimes;
	}
}
