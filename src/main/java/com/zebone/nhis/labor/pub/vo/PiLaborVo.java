package com.zebone.nhis.labor.pub.vo;

import java.util.Date;

import com.zebone.nhis.common.module.labor.nis.PiLabor;

public class PiLaborVo extends PiLabor {
	private String pkPv;
	private String codeHp;
	private String codeIp;//住院号
	private String codePi;//患者编码
	private String namePi;
    private String idNo;
    private Date birthDate;
    private String mobile;
    private String dtOccu;
    private String dtEdu;
    private String nameCompany;
    private String dtCountry;
    private String dtNation;
    private String dtBlood;
    private String addrCur;//现住址
    private String addrcodeCur;//现住址编码
    private String addrCurDt;
    private String addrRegi;//户口地址
    private String addrcodeRegi;//现住址编码
    private String addrRegiDt;
    private String occu;//母亲职业
    private String blood;//母亲血型
    private String occuHus;//父亲职业
    private String bloodHus;//父亲血型

	public String getOccu() {
		return occu;
	}

	public void setOccu(String occu) {
		this.occu = occu;
	}

	public String getBlood() {
		return blood;
	}

	public void setBlood(String blood) {
		this.blood = blood;
	}

	public String getOccuHus() {
		return occuHus;
	}

	public void setOccuHus(String occuHus) {
		this.occuHus = occuHus;
	}

	public String getBloodHus() {
		return bloodHus;
	}

	public void setBloodHus(String bloodHus) {
		this.bloodHus = bloodHus;
	}

	public String getCodeHp() {
		return codeHp;
	}

	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	public String getAddrcodeCur() {
		return addrcodeCur;
	}

	public void setAddrcodeCur(String addrcodeCur) {
		this.addrcodeCur = addrcodeCur;
	}

	public String getAddrcodeRegi() {
		return addrcodeRegi;
	}

	public void setAddrcodeRegi(String addrcodeRegi) {
		this.addrcodeRegi = addrcodeRegi;
	}

	public void setCodeHp(String codeHp) {
		this.codeHp = codeHp;
	}

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
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

	public String getDtEdu() {
		return dtEdu;
	}

	public void setDtEdu(String dtEdu) {
		this.dtEdu = dtEdu;
	}

	public String getNameCompany() {
		return nameCompany;
	}

	public void setNameCompany(String nameCompany) {
		this.nameCompany = nameCompany;
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

	public String getDtBlood() {
		return dtBlood;
	}

	public void setDtBlood(String dtBlood) {
		this.dtBlood = dtBlood;
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

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

}
