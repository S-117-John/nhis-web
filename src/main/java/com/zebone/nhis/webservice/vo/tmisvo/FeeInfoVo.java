package com.zebone.nhis.webservice.vo.tmisvo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.zebone.nhis.common.support.XmlElementAnno;

/**
 * 输血请求参数构造xml
 * 计费信息
 * @author frank
 *
 */
@XmlRootElement(name = "FeeInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class FeeInfoVo {
	@XmlElement(name = "ID")
	public String iD;
	@XmlElement(name = "CounteractListID")
	public BigDecimal counteractListID;
	@XmlElement(name = "HospHISCode")
	public String hospHISCode;
	@XmlElement(name = "CaseNum")
	public String caseNum;
	@XmlElement(name = "InHospitalID")
	public String inHospitalID;
	@XmlElement(name = "VisitID")
	public String visitID;
	
	@XmlElement(name = "CheckABORH")
	public String checkABORH;
	
	@XmlElement(name = "FeeItemHISCode")
	public String feeItemHISCode;
	
	@XmlElement(name = "Price")
	public BigDecimal price;
	
	@XmlElement(name = "Amount")
	public BigDecimal amount;
	
	@XmlElement(name = "Money")
	public BigDecimal money;
	
	@XmlElement(name = "FeeTypeID")
	public String feeTypeID;
	
	@XmlElement(name = "FeeSourceID")
	public String feeSourceID;
	
	@XmlElement(name = "BusinessDate")
	public String businessDate;
	
	@XmlElement(name = "DonCode")
	public String donCode;
	
	@XmlElement(name = "ProdCode")
	public String prodCode;
	
	@XmlElement(name = "BloodABORH")
	public String bloodABORH;
	
	@XmlElement(name = "ApplyNum")
	public String applyNum;
	
	@XmlElement(name = "ApplyDocCode")
	public String applyDocCode;
	
	@XmlElement(name = "ApplyDocName")
	public String applyDocName;
	
	@XmlElement(name = "ApplyDeptHISCode")
	public String applyDeptHISCode;
	
	@XmlElement(name = "ApplyDeptName")
	public String applyDeptName;
	
	@XmlElement(name = "ApplyDate")
	public String applyDate;
	
	@XmlElement(name = "PreTransDate")
	public String preTransDate;
	
	@XmlElement(name = "IssueDate")
	public String issueDate;
	
	@XmlElement(name = "TollCollectorCode")
	public String tollCollectorCode;
	
	@XmlElement(name = "TollCollectorName")
	public String tollCollectorName;
	
	@XmlElement(name = "TransDeptHISCode")
	public String transDeptHISCode;
	
	@XmlElement(name = "TransBloodDeptName")
	public String transBloodDeptName;

	@XmlElement(name = "BusiBillPersionHISCode")
	public String busiBillPersionHISCode;
	
	@XmlElement(name = "BusiBillPersionName")
	public String busiBillPersionName;
	
	@XmlElement(name = "ChargerHISCode")
	public String chargerHISCode;
	
	@XmlElement(name = "DoctorAdviceNumFeeListCoun")
	public String doctorAdviceNumFeeListCoun;
	
	@XmlElement(name = "DoctorAdviceNum")
	public String doctorAdviceNum;
	
	
	public String getiD() {
		return iD;
	}

	public void setiD(String iD) {
		this.iD = iD;
	}

	public BigDecimal getCounteractListID() {
		return counteractListID;
	}

	public void setCounteractListID(BigDecimal counteractListID) {
		this.counteractListID = counteractListID;
	}
	
	public String getHospHISCode() {
		return hospHISCode;
	}

	public void setHospHISCode(String hospHISCode) {
		this.hospHISCode = hospHISCode;
	}
	
	public String getCaseNum() {
		return caseNum;
	}

	public void setCaseNum(String caseNum) {
		this.caseNum = caseNum;
	}
	
	public String getInHospitalID() {
		return inHospitalID;
	}

	public void setInHospitalID(String inHospitalID) {
		this.inHospitalID = inHospitalID;
	}
	
	public String getVisitID() {
		return visitID;
	}

	public void setVisitID(String visitID) {
		this.visitID = visitID;
	}
	
	public String getCheckABORH() {
		return checkABORH;
	}

	public void setCheckABORH(String checkABORH) {
		this.checkABORH = checkABORH;
	}
	
	public String getFeeItemHISCode() {
		return feeItemHISCode;
	}

	public void setFeeItemHISCode(String feeItemHISCode) {
		this.feeItemHISCode = feeItemHISCode;
	}
	
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	
	public String getFeeTypeID() {
		return feeTypeID;
	}

	public void setFeeTypeID(String feeTypeID) {
		this.feeTypeID = feeTypeID;
	}
	
	public String getFeeSourceID() {
		return feeSourceID;
	}

	public void setFeeSourceID(String feeSourceID) {
		this.feeSourceID = feeSourceID;
	}
	
	public String getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(String businessDate) {
		this.businessDate = businessDate;
	}
	
	public String getDonCode() {
		return donCode;
	}

	public void setDonCode(String donCode) {
		this.donCode = donCode;
	}
	
	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	
	public String getBloodABORH() {
		return bloodABORH;
	}

	public void setBloodABORH(String bloodABORH) {
		this.bloodABORH = bloodABORH;
	}
	
	public String getApplyNum() {
		return applyNum;
	}

	public void setApplyNum(String applyNum) {
		this.applyNum = applyNum;
	}
	
	public String getApplyDocCode() {
		return applyDocCode;
	}

	public void setApplyDocCode(String applyDocCode) {
		this.applyDocCode = applyDocCode;
	}
	
	public String getApplyDocName() {
		return applyDocName;
	}

	public void setApplyDocName(String applyDocName) {
		this.applyDocName = applyDocName;
	}
	
	public String getApplyDeptHISCode() {
		return applyDeptHISCode;
	}

	public void setApplyDeptHISCode(String applyDeptHISCode) {
		this.applyDeptHISCode = applyDeptHISCode;
	}
	
	public String getApplyDeptName() {
		return applyDeptName;
	}

	public void setApplyDeptName(String applyDeptName) {
		this.applyDeptName = applyDeptName;
	}
	
	public String getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	
	public String getPreTransDate() {
		return preTransDate;
	}

	public void setPreTransDate(String preTransDate) {
		this.preTransDate = preTransDate;
	}
	
	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	
	public String getTollCollectorCode() {
		return tollCollectorCode;
	}

	public void setTollCollectorCode(String tollCollectorCode) {
		this.tollCollectorCode = tollCollectorCode;
	}
	
	public String getTollCollectorName() {
		return tollCollectorName;
	}

	public void setTollCollectorName(String tollCollectorName) {
		this.tollCollectorName = tollCollectorName;
	}
	
	public String getTransDeptHISCode() {
		return transDeptHISCode;
	}

	public void setTransDeptHISCode(String transDeptHISCode) {
		this.transDeptHISCode = transDeptHISCode;
	}
	
	public String getTransBloodDeptName() {
		return transBloodDeptName;
	}

	public void setTransBloodDeptName(String transBloodDeptName) {
		this.transBloodDeptName = transBloodDeptName;
	}
	
	public String getBusiBillPersionHISCode() {
		return busiBillPersionHISCode;
	}

	public void setBusiBillPersionHISCode(String busiBillPersionHISCode) {
		this.busiBillPersionHISCode = busiBillPersionHISCode;
	}
	
	public String getBusiBillPersionName() {
		return busiBillPersionName;
	}

	public void setBusiBillPersionName(String busiBillPersionName) {
		this.busiBillPersionName = busiBillPersionName;
	}
	
	public String getChargerHISCode() {
		return chargerHISCode;
	}

	public void setChargerHISCode(String chargerHISCode) {
		this.chargerHISCode = chargerHISCode;
	}
	
	public String getDoctorAdviceNumFeeListCoun() {
		return doctorAdviceNumFeeListCoun;
	}

	public void setDoctorAdviceNumFeeListCoun(String doctorAdviceNumFeeListCoun) {
		this.doctorAdviceNumFeeListCoun = doctorAdviceNumFeeListCoun;
	}
	
	public String getDoctorAdviceNum() {
		return doctorAdviceNum;
	}

	public void setDoctorAdviceNum(String doctorAdviceNum) {
		this.doctorAdviceNum = doctorAdviceNum;
	}
}
