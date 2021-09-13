package com.zebone.nhis.ma.pub.syx.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="req")
public class SettleUpReq {
	@XmlElement(name="pkPv")
	private String pkPv;
	
	@XmlElement(name="codeIp")
	private String codeIp;

	@XmlElement(name="pkPi")
	private String pkPi;

	@XmlElement(name="codePi")
	private String codePi;
	
	@XmlElement(name="namePi")
	private String namePi;
	
	@XmlElement(name="dtSex")
	private String dtSex;
	
	@XmlElement(name="birthDate")
	private String birthDate;
	
	@XmlElement(name="dateBegin")
	private String dateBegin;
	
	@XmlElement(name="dateEnd")
	private String dateEnd;
	
	@XmlElement(name="inDays")
	private String inDays;
	
	@XmlElement(name="pkInsu")
	private String pkInsu;
	
	@XmlElement(name="nameInsu")
	private String nameInsu;
	
	@XmlElement(name="dtOutcomes")
	private String dtOutcomes;
	
	@XmlElement(name="euSettleWay")
	private String euSettleWay;
	
	@XmlElement(name="totalAmount")
	private BigDecimal totalAmount;
	
	@XmlElement(name="foregiftAmount")
	private BigDecimal foregiftAmount;
	
	@XmlElement(name="unpaidAmount")
	private BigDecimal unpaidAmount;
	
	@XmlElement(name="insuAmount")
	private BigDecimal insuAmount;
	
	@XmlElement(name="selfAmout")
	private BigDecimal selfAmout;
	
	@XmlElement(name="payAmount")
	private BigDecimal payAmount;

	@XmlElement(name="codeDept")
	private String codeDept;

	@XmlElement(name="nameDept")
	private String nameDept;

	@XmlElement(name="userCardId")
	private String userCardId;

	@XmlElement(name="ipTimes")
	private String ipTimes;

	@XmlElement(name="codeEmpTre")
	private String codeEmpTre;

	@XmlElement(name="nameEmpTre")
	private String nameEmpTre;

	@XmlElement(name="settleStatus")
	private String settleStatus;

	@XmlElement(name="pkDeptSt")
	private String pkDeptSt;

	@XmlElement(name="pkEmpSt")
	private String pkEmpSt;

	@XmlElement(name="nameEmpSt")
	private String nameEmpSt;

	public String getPkDeptSt() {
		return pkDeptSt;
	}

	public void setPkDeptSt(String pkDeptSt) {
		this.pkDeptSt = pkDeptSt;
	}

	public String getPkEmpSt() {
		return pkEmpSt;
	}

	public void setPkEmpSt(String pkEmpSt) {
		this.pkEmpSt = pkEmpSt;
	}

	public String getNameEmpSt() {
		return nameEmpSt;
	}

	public void setNameEmpSt(String nameEmpSt) {
		this.nameEmpSt = nameEmpSt;
	}

	public String getSettleStatus() {
		return settleStatus;
	}

	public void setSettleStatus(String settleStatus) {
		this.settleStatus = settleStatus;
	}

	public String getCodeEmpTre() {
		return codeEmpTre;
	}

	public void setCodeEmpTre(String codeEmpTre) {
		this.codeEmpTre = codeEmpTre;
	}

	public String getNameEmpTre() {
		return nameEmpTre;
	}

	public void setNameEmpTre(String nameEmpTre) {
		this.nameEmpTre = nameEmpTre;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
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

	public String getUserCardId() {
		return userCardId;
	}

	public void setUserCardId(String userCardId) {
		this.userCardId = userCardId;
	}

	public String getIpTimes() {
		return ipTimes;
	}

	public void setIpTimes(String ipTimes) {
		this.ipTimes = ipTimes;
	}

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

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getInDays() {
		return inDays;
	}

	public void setInDays(String inDays) {
		this.inDays = inDays;
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

	public String getDtOutcomes() {
		return dtOutcomes;
	}

	public void setDtOutcomes(String dtOutcomes) {
		this.dtOutcomes = dtOutcomes;
	}

	public String getEuSettleWay() {
		return euSettleWay;
	}

	public void setEuSettleWay(String euSettleWay) {
		this.euSettleWay = euSettleWay;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getForegiftAmount() {
		return foregiftAmount;
	}

	public void setForegiftAmount(BigDecimal foregiftAmount) {
		this.foregiftAmount = foregiftAmount;
	}

	public BigDecimal getUnpaidAmount() {
		return unpaidAmount;
	}

	public void setUnpaidAmount(BigDecimal unpaidAmount) {
		this.unpaidAmount = unpaidAmount;
	}

	public BigDecimal getInsuAmount() {
		return insuAmount;
	}

	public void setInsuAmount(BigDecimal insuAmount) {
		this.insuAmount = insuAmount;
	}

	public BigDecimal getSelfAmout() {
		return selfAmout;
	}

	public void setSelfAmout(BigDecimal selfAmout) {
		this.selfAmout = selfAmout;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
}
