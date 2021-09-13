package com.zebone.nhis.ma.pub.platform.syx.vo.scmhr;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CONSIS_PRESC_MSTVW")
public class ConsisPrescMstvw {
	
	private String pkPresocc;
	@XmlElement(name = "PRESC_DATE")
	private String prescDate;
	@XmlElement(name = "PRESC_NO")
	private String prescNo;
	@XmlElement(name = "DISPENSARY")
	private String dispensary;
	@XmlElement(name = "PATIENT_ID")
	private String patientId;
	@XmlElement(name = "PATIENT_NAME")
	private String patientName;
	@XmlElement(name = "INVOICE_NO")
	private String invoiceNo;
	@XmlElement(name = "PATIENT_TYPE")
	private String patientType;
	@XmlElement(name = "DATE_OF_BIRTH")
	private String dateOfBirth;
	@XmlElement(name = "SEX")
	private String sex;
	@XmlElement(name = "PRESC_IDENTITY")
	private String prescIdentity;
	@XmlElement(name = "CHARGE_TYPE")
	private String chargeType;
	@XmlElement(name = "PRESC_ATTR")
	private String prescAttr;
	@XmlElement(name = "PRESC_INFO")
	private String prescInfo;
	@XmlElement(name = "RCPT_INFO")
	private String rcptInfo;
	@XmlElement(name = "RCPT_REMARK")
	private String rcptRemark;
	@XmlElement(name = "REPETITION")
	private String repetition;
	@XmlElement(name = "COSTS")
	private String costs;
	@XmlElement(name = "PAYMENTS")
	private String payments;
	@XmlElement(name = "ORDERED_BY")
	private String orderedBy;
	@XmlElement(name = "ORDERED_BY_NAME")
	private String orderedByName;
	@XmlElement(name = "PRESCRIBED_BY")
	private String prescribedBy;
	@XmlElement(name = "ENTERED_BY")
	private String enteredBy;
	@XmlElement(name = "DISPENSE_PRI")
	private String dispensePri;
	@XmlElement(name = "CONSIS_PRESC_DTLVW")
	private List<ConsisPrescDtlvw> consisPrescDtlvw;
	
	public String getPkPresocc() {
		return pkPresocc;
	}
	public void setPkPresocc(String pkPresocc) {
		this.pkPresocc = pkPresocc;
	}
	public String getPrescDate() {
		return prescDate;
	}
	public void setPrescDate(String prescDate) {
		this.prescDate = prescDate;
	}
	public String getPrescNo() {
		return prescNo;
	}
	public void setPrescNo(String prescNo) {
		this.prescNo = prescNo;
	}
	public String getDispensary() {
		return dispensary;
	}
	public void setDispensary(String dispensary) {
		this.dispensary = dispensary;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getPatientType() {
		return patientType;
	}
	public void setPatientType(String patientType) {
		this.patientType = patientType;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPrescIdentity() {
		return prescIdentity;
	}
	public void setPrescIdentity(String prescIdentity) {
		this.prescIdentity = prescIdentity;
	}
	public String getChargeType() {
		return chargeType;
	}
	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}
	public String getPrescAttr() {
		return prescAttr;
	}
	public void setPrescAttr(String prescAttr) {
		this.prescAttr = prescAttr;
	}
	public String getPrescInfo() {
		return prescInfo;
	}
	public void setPrescInfo(String prescInfo) {
		this.prescInfo = prescInfo;
	}
	public String getRcptInfo() {
		return rcptInfo;
	}
	public void setRcptInfo(String rcptInfo) {
		this.rcptInfo = rcptInfo;
	}
	public String getRcptRemark() {
		return rcptRemark;
	}
	public void setRcptRemark(String rcptRemark) {
		this.rcptRemark = rcptRemark;
	}
	public String getRepetition() {
		return repetition;
	}
	public void setRepetition(String repetition) {
		this.repetition = repetition;
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
	public String getOrderedBy() {
		return orderedBy;
	}
	public void setOrderedBy(String orderedBy) {
		this.orderedBy = orderedBy;
	}
	public String getOrderedByName() {
		return orderedByName;
	}
	public void setOrderedByName(String orderedByName) {
		this.orderedByName = orderedByName;
	}
	public String getPrescribedBy() {
		return prescribedBy;
	}
	public void setPrescribedBy(String prescribedBy) {
		this.prescribedBy = prescribedBy;
	}
	public String getEnteredBy() {
		return enteredBy;
	}
	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}
	public String getDispensePri() {
		return dispensePri;
	}
	public void setDispensePri(String dispensePri) {
		this.dispensePri = dispensePri;
	}
	public List<ConsisPrescDtlvw> getConsisPrescDtlvw() {
		if(consisPrescDtlvw==null) consisPrescDtlvw=new ArrayList<ConsisPrescDtlvw>();
		return consisPrescDtlvw;
	}
	public void setConsisPrescDtlvw(List<ConsisPrescDtlvw> consisPrescDtlvw) {
		this.consisPrescDtlvw = consisPrescDtlvw;
	}
}
