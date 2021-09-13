package com.zebone.nhis.webservice.syx.vo.scmhr;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "data_prescription_detail")
public class DataPrescriptionDetail {
	
	private String pkPresocc;
	@XmlElement(name = "id")
	private String id;
	@XmlElement(name = "granule_id")
	private String granuleId;
	@XmlElement(name = "granule_name")
	private String granuleName;
	@XmlElement(name = "dose_herb")
	private BigDecimal doseHerb;
	@XmlElement(name = "equivalent")
	private String equivalent;
	@XmlElement(name = "dose")
	private BigDecimal dose;
	@XmlElement(name = "price_cg")
	private BigDecimal priceCg;
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
	private BigDecimal payments;
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
	@XmlElement(name="quantity_day")
	private String quantityDay;
	@XmlElement(name="frequency_name")
	private String frequencyName;
	
	
	public String getPkPresocc() {
		return pkPresocc;
	}
	public void setPkPresocc(String pkPresocc) {
		this.pkPresocc = pkPresocc;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public BigDecimal getDoseHerb() {
		return doseHerb;
	}
	public void setDoseHerb(BigDecimal doseHerb) {
		this.doseHerb = doseHerb;
	}
	public String getEquivalent() {
		return equivalent;
	}
	public void setEquivalent(String equivalent) {
		this.equivalent = equivalent;
	}
	public BigDecimal getDose() {
		return dose;
	}
	public void setDose(BigDecimal dose) {
		this.dose = dose;
	}
	public BigDecimal getPriceCg() {
		return priceCg;
	}
	public void setPriceCg(BigDecimal priceCg) {
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
	public BigDecimal getPayments() {
		return payments;
	}
	public void setPayments(BigDecimal payments) {
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
	public String getQuantityDay() {
		return quantityDay;
	}
	public void setQuantityDay(String quantityDay) {
		this.quantityDay = quantityDay;
	}
	public String getFrequencyName() {
		return frequencyName;
	}
	public void setFrequencyName(String frequencyName) {
		this.frequencyName = frequencyName;
	}
	
	
	
}
