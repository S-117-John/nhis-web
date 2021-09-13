package com.zebone.nhis.ma.pub.platform.syx.vo.scmhr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CONSIS_PRESC_DTLVW")
public class ConsisPrescDtlvw {
	
	private String pkPresocc;
	@XmlElement(name = "PRESC_NO")
	private String prescNo;
	@XmlElement(name = "ITEM_NO")
	private String itemNo;
	@XmlElement(name = "ADVICE_CODE")
	private String adviceCode;
	@XmlElement(name = "DRUG_CODE")
	private String drugCode;
	@XmlElement(name = "DRUG_SPEC")
	private String drugSpec;
	@XmlElement(name = "DRUG_NAME")
	private String drugName;
	@XmlElement(name = "FIRM_ID")
	private String firmId;
	@XmlElement(name = "FIRM_NAME")
	private String firmName;
	@XmlElement(name = "PACKAGE_SPEC")
	private String packageSpec;
	@XmlElement(name = "PACKAGE_UNITS")
	private String packageUnits;
	@XmlElement(name = "QUANTITY")
	private String quantity;
	@XmlElement(name = "UNIT")
	private String unit;
	@XmlElement(name = "COSTS")
	private String costs;
	@XmlElement(name = "PAYMENTS")
	private String payments;
	@XmlElement(name = "DOSAGE")
	private String dosage;
	@XmlElement(name = "DOSAGE_UNITS")
	private String dosageUnits;
	@XmlElement(name = "ADMINISTRATION")
	private String administration;
	@XmlElement(name = "FREQUENCY")
	private String frequency;
	@XmlElement(name = "ADDITIONUSAGE")
	private String additionusage;
	@XmlElement(name = "RCPT_REMARK")
	private String rcptRemark;
	
	public String getPkPresocc() {
		return pkPresocc;
	}
	public void setPkPresocc(String pkPresocc) {
		this.pkPresocc = pkPresocc;
	}
	public String getPrescNo() {
		return prescNo;
	}
	public void setPrescNo(String prescNo) {
		this.prescNo = prescNo;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getAdviceCode() {
		return adviceCode;
	}
	public void setAdviceCode(String adviceCode) {
		this.adviceCode = adviceCode;
	}
	public String getDrugCode() {
		return drugCode;
	}
	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}
	public String getDrugSpec() {
		return drugSpec;
	}
	public void setDrugSpec(String drugSpec) {
		this.drugSpec = drugSpec;
	}
	public String getDrugName() {
		return drugName;
	}
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	public String getFirmId() {
		return firmId;
	}
	public void setFirmId(String firmId) {
		this.firmId = firmId;
	}
	public String getFirmName() {
		return firmName;
	}
	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}
	public String getPackageSpec() {
		return packageSpec;
	}
	public void setPackageSpec(String packageSpec) {
		this.packageSpec = packageSpec;
	}
	public String getPackageUnits() {
		return packageUnits;
	}
	public void setPackageUnits(String packageUnits) {
		this.packageUnits = packageUnits;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getCosts() {
		return costs;
	}
	public void setCosts(String costs) {
		this.costs = costs;
	}
	public String getPayments() {
		return payments;
	}
	public void setPayments(String payments) {
		this.payments = payments;
	}
	public String getDosage() {
		return dosage;
	}
	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	public String getDosageUnits() {
		return dosageUnits;
	}
	public void setDosageUnits(String dosageUnits) {
		this.dosageUnits = dosageUnits;
	}
	public String getAdministration() {
		return administration;
	}
	public void setAdministration(String administration) {
		this.administration = administration;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getAdditionusage() {
		return additionusage;
	}
	public void setAdditionusage(String additionusage) {
		this.additionusage = additionusage;
	}
	public String getRcptRemark() {
		return rcptRemark;
	}
	public void setRcptRemark(String rcptRemark) {
		this.rcptRemark = rcptRemark;
	}
	
	
}
