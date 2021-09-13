package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/*
 * 门诊患者查询信息响应
 */
@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbQueOpPvInfoResTemVo {

	@XmlElement(name="patientId")
	private String patientid;
	@XmlElement(name="codePi")
	private String codePi;
	@XmlElement(name="codeOp")
	private String codeOp;
	@XmlElement(name="codeIp")
	private String codeIp;
	@XmlElement(name="cardStatus")
	private String cardStatus;
	@XmlElement(name="dtCardType")
	private String dtCardType;
	@XmlElement(name="dtcardNo")
	private String dtcardNo;
	@XmlElement(name="name")
	private String name;
	@XmlElement(name="sex")
	private String sex;
	@XmlElement(name="age")
	private String age;
	@XmlElement(name="birthday")
	private String birthday;
	@XmlElement(name="idCardType")
	private String idCardType;
	@XmlElement(name="idCardNo")
	private String idCardNo;
	@XmlElement(name="phoneNo")
	private String phoneNo;
	@XmlElement(name="nation")
	private String nation;
	@XmlElement(name="address")
	private String address;
	
	@XmlElement(name="visitId")
	private String visitId;
	
	@XmlElement(name="visitDate")
	private String visitDate;
	@XmlElement(name="deptName")
	private String deptName;
	
	@XmlElement(name="doctName")
	private String doctName;
	//患者可用余额
	@XmlElement(name="balance")
	private String  balance;
	//代缴费用
	@XmlElement(name="totalFee")
	private String totalFee;
	public String getVisitId() {
		return visitId;
	}
	public void setVisitId(String visitId) {
		this.visitId = visitId;
	}
	public String getVisitDate() {
		return visitDate;
	}
	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDoctName() {
		return doctName;
	}
	public void setDoctName(String doctName) {
		this.doctName = doctName;
	}
	public String getPatientid() {
		return patientid;
	}
	public void setPatientid(String patientid) {
		this.patientid = patientid;
	}
	public String getCodePi() {
		return codePi;
	}
	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}
	public String getCodeOp() {
		return codeOp;
	}
	public void setCodeOp(String codeOp) {
		this.codeOp = codeOp;
	}
	public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
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
	public String getIdCardType() {
		return idCardType;
	}
	public void setIdCardType(String idCardType) {
		this.idCardType = idCardType;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
	
}
