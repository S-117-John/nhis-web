package com.zebone.nhis.ma.pub.platform.syx.vo.scmhr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "data_prescription_detail")
public class DataPrescriptionDetail {
	
	private String pkPresocc;
	@XmlElement(name = "granule_id")
	private String granuleId;
	@XmlElement(name = "granule_name")
	private String granuleName;
	@XmlElement(name = "dose_herb")
	private String doseHerb;
	@XmlElement(name = "equivalent")
	private String equivalent;
	@XmlElement(name = "dose")
	private String dose;
	@XmlElement(name = "price_cg")
	private String priceCg;
	@XmlElement(name = "item_no")
	private String itemNo;
	@XmlElement(name = "advice_code")
	private String adviceCode;
	@XmlElement(name = "drug_spec")
	private String drugSpec;
	@XmlElement(name = "firm_id")
	private String firmId;
	@XmlElement(name = "firm_name")
	private String firmName;
	@XmlElement(name = "package_spec")
	private String packageSpec;
	@XmlElement(name = "package_units")
	private String packageUnits;
	@XmlElement(name = "quantity")
	private String quantity;
	@XmlElement(name = "unit")
	private String unit;
	@XmlElement(name = "payments")
	private String payments;
	@XmlElement(name = "dosage_units")
	private String dosageUnits;
	@XmlElement(name = "administration")
	private String administration;
	@XmlElement(name = "frequency")
	private String frequency;
	@XmlElement(name = "additionusage")
	private String additionusage;
	@XmlElement(name = "rcpt_remark")
	private String rcptRemark;
	
	
	
	public String getPkPresocc() {
		return pkPresocc;
	}
	public void setPkPresocc(String pkPresocc) {
		this.pkPresocc = pkPresocc;
	}
	public String getGranuleId() {
		return granuleId;
	}
	public void setGranuleId(String granuleId) {
		this.granuleId = granuleId;
	}
	public String getGranuleName() {
		return granuleName;
	}
	public void setGranuleName(String granuleName) {
		this.granuleName = granuleName;
	}
	public String getDoseHerb() {
		return doseHerb;
	}
	public void setDoseHerb(String doseHerb) {
		this.doseHerb = doseHerb;
	}
	public String getEquivalent() {
		return equivalent;
	}
	public void setEquivalent(String equivalent) {
		this.equivalent = equivalent;
	}
	public String getDose() {
		return dose;
	}
	public void setDose(String dose) {
		this.dose = dose;
	}
	public String getPriceCg() {
		return priceCg;
	}
	public void setPriceCg(String priceCg) {
		this.priceCg = priceCg;
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
	public String getDrugSpec() {
		return drugSpec;
	}
	public void setDrugSpec(String drugSpec) {
		this.drugSpec = drugSpec;
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
	public String getPayments() {
		return payments;
	}
	public void setPayments(String payments) {
		this.payments = payments;
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
