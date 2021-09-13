package com.zebone.nhis.webservice.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "payInfos")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbQueOpUnpaidResTemVo {

	@XmlElement(name="visitId")
	private String visitId;
	//结算类型
	@XmlElement(name="settleType")
	private String settleType;
	@XmlElement(name="deptName")
	private String deptName;
	@XmlElement(name="doctName")
	private String doctName;
	@XmlElement(name="totalFee")
	private String totalFee;
	@XmlElement(name="visitDate")
	private String visitDate;
	@XmlElement(name="diagnosis")
	private String diagnosis;
	//账户总额
	@XmlElement(name="balance")
	private String balance;
	//可用余额
	@XmlElement(name="usable")
	private String usable;
	
	@XmlElement(name = "recipes")
	private List<LbRecipesVo> recipes;
	
	public String getVisitId() {
		return visitId;
	}
	public void setVisitId(String visitId) {
		this.visitId = visitId;
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
	public String getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
	public String getVisitDate() {
		return visitDate;
	}
	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public List<LbRecipesVo> getRecipes() {
		return recipes;
	}
	public void setRecipes(List<LbRecipesVo> recipes) {
		this.recipes = recipes;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getUsable() {
		return usable;
	}
	public void setUsable(String usable) {
		this.usable = usable;
	}
	public String getSettleType() {
		return settleType;
	}
	public void setSettleType(String settleType) {
		this.settleType = settleType;
	}
	
}
