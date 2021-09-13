package com.zebone.nhis.webservice.vo.tmisvo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 输血返回构造xml
 * @author frank
 *费用
 */
@XmlRootElement(name = "HISFee")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseHISFeeVo {
	@XmlElement(name = "ID")
	public String iD;
	
	@XmlElement(name = "HospHISCode")
	public String hospHISCode;
	
	@XmlElement(name = "HospName")
	public String hospName;
	
	@XmlElement(name = "InHospitalID")
	public String inHospitalID;
	
	@XmlElement(name = "CaseNum")
	public String caseNum;
	
	@XmlElement(name = "InHospitalName")
	public String inHospitalName;
	
	@XmlElement(name = "PatientName")
	public String patientName;
	
	@XmlElement(name = "ChargeName")
	public String chargeName;
	
	@XmlElement(name = "Price")
	public String price;
	
	@XmlElement(name = "Unit")
	public String unit;
	
	@XmlElement(name = "Amount")
	public String amount;
	
	@XmlElement(name = "Money")
	public String money;
	
	@XmlElement(name = "TollCollectorName")
	public String tollCollectorName;
	
	@XmlElement(name = "ChargeDate")
	public String chargeDate;
	
	@XmlElement(name = "BusinessDate")
	public String businessDate;
	
	@XmlElement(name = "DoctorAdviceNum")
	public String doctorAdviceNum;

	public String getiD() {
		return iD;
	}

	public void setiD(String iD) {
		this.iD = iD;
	}

	public String getHospHISCode() {
		return hospHISCode;
	}

	public void setHospHISCode(String hospHISCode) {
		this.hospHISCode = hospHISCode;
	}

	public String getHospName() {
		return hospName;
	}

	public void setHospName(String hospName) {
		this.hospName = hospName;
	}

	public String getInHospitalID() {
		return inHospitalID;
	}

	public void setInHospitalID(String inHospitalID) {
		this.inHospitalID = inHospitalID;
	}

	public String getCaseNum() {
		return caseNum;
	}

	public void setCaseNum(String caseNum) {
		this.caseNum = caseNum;
	}

	public String getInHospitalName() {
		return inHospitalName;
	}

	public void setInHospitalName(String inHospitalName) {
		this.inHospitalName = inHospitalName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getChargeName() {
		return chargeName;
	}

	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getTollCollectorName() {
		return tollCollectorName;
	}

	public void setTollCollectorName(String tollCollectorName) {
		this.tollCollectorName = tollCollectorName;
	}

	public String getChargeDate() {
		return chargeDate;
	}

	public void setChargeDate(String chargeDate) {
		this.chargeDate = chargeDate;
	}

	public String getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(String businessDate) {
		this.businessDate = businessDate;
	}

	public String getDoctorAdviceNum() {
		return doctorAdviceNum;
	}

	public void setDoctorAdviceNum(String doctorAdviceNum) {
		this.doctorAdviceNum = doctorAdviceNum;
	}

	

}
