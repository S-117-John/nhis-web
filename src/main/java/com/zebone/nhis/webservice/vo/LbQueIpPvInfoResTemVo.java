package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbQueIpPvInfoResTemVo {

	@XmlElement(name = "respCommon")
    private RespCommonVo respCommon;
	
	@XmlElement(name="status")
	private String euStatus;
	@XmlElement(name="inPatientNo")
	private String inPatientNo;
	@XmlElement(name="balance")
	private String balance;
	@XmlElement(name="name")
	private String name;
	@XmlElement(name="sex")
	private String sex;
	@XmlElement(name="age")
	private String age;
	@XmlElement(name="birthday")
	private String birthday;
	@XmlElement(name="phoneNo")
	private String phoneNo;
	@XmlElement(name="deptName")
	private String deptName;
	@XmlElement(name="areaNo")
	private String areaNo;
	@XmlElement(name="bedNo")
	private String bedNo;
	@XmlElement(name="inDate")
	private String inDate;
	@XmlElement(name="totalPrePayment")
	private String totalPrePayment;
	@XmlElement(name="totalCost")
	private String totalCost;
	@XmlElement(name="isSupportCancel")
	private String isSupportCancel;
	@XmlElement(name="idCardNo")
	private String idCardNo;
	@XmlElement(name="mrId")
	private String mrId;
	@XmlElement(name="elecCertId")
	private String elecCertId;
	@XmlElement(name="insureType")
	private String insureType;
	@XmlElement(name="wristbandInfo")
	private String wristbandInfo;
	@XmlElement(name="wristbandPrintNum")
	private String wristbandPrintNum;
	@XmlElement(name="cardStatus")
	private String cardStatus;
	@XmlElement(name="dtCardType")
	private String dtCardType;
	@XmlElement(name="dtcardNo")
	private String dtcardNo;
	private String pkPv;
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getEuStatus() {
		return euStatus;
	}
	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}
	
	public String getInPatientNo() {
		return inPatientNo;
	}
	public void setInPatientNo(String inPatientNo) {
		this.inPatientNo = inPatientNo;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getAreaNo() {
		return areaNo;
	}
	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getInDate() {
		return inDate;
	}
	public void setInDate(String inDate) {
		this.inDate = inDate;
	}
	public String getTotalPrePayment() {
		return totalPrePayment;
	}
	public void setTotalPrePayment(String totalPrePayment) {
		this.totalPrePayment = totalPrePayment;
	}
	public String getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}
	public String getIsSupportCancel() {
		return isSupportCancel;
	}
	public void setIsSupportCancel(String isSupportCancel) {
		this.isSupportCancel = isSupportCancel;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public String getMrId() {
		return mrId;
	}
	public void setMrId(String mrId) {
		this.mrId = mrId;
	}
	public String getElecCertId() {
		return elecCertId;
	}
	public void setElecCertId(String elecCertId) {
		this.elecCertId = elecCertId;
	}
	public String getInsureType() {
		return insureType;
	}
	public void setInsureType(String insureType) {
		this.insureType = insureType;
	}
	public String getWristbandInfo() {
		return wristbandInfo;
	}
	public void setWristbandInfo(String wristbandInfo) {
		this.wristbandInfo = wristbandInfo;
	}
	public String getWristbandPrintNum() {
		return wristbandPrintNum;
	}
	public void setWristbandPrintNum(String wristbandPrintNum) {
		this.wristbandPrintNum = wristbandPrintNum;
	}
	public RespCommonVo getRespCommon() {
		return respCommon;
	}
	public void setRespCommon(RespCommonVo respCommon) {
		this.respCommon = respCommon;
	}
	public String getCardStatus() {
		return cardStatus;
	}
	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}
	public String getDtCardType() {
		return dtCardType;
	}
	public void setDtCardType(String dtCardType) {
		this.dtCardType = dtCardType;
	}
	public String getDtcardNo() {
		return dtcardNo;
	}
	public void setDtcardNo(String dtcardNo) {
		this.dtcardNo = dtcardNo;
	}
	
}
