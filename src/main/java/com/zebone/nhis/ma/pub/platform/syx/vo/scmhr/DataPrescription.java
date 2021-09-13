package com.zebone.nhis.ma.pub.platform.syx.vo.scmhr;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "data_prescription")
public class DataPrescription {
	private String pkPresocc;
	@XmlElement(name = "autoid")
	private String autoid;
	@XmlElement(name = "id")
	private String id;
	@XmlElement(name = "register_id")
	private String registerId;
	@XmlElement(name = "code_pvtype")
	private String codePvtype;
	@XmlElement(name = "code_pati")
	private String codePati;
	@XmlElement(name = "name")
	private String name;
	@XmlElement(name = "age")
	private String age;
	@XmlElement(name = "tele")
	private String tele;
	@XmlElement(name = "email")
	private String email;
	@XmlElement(name = "department_name")
	private String departmentName;
	@XmlElement(name = "doctor_name")
	private String doctorName;
	@XmlElement(name = "prescription_name")
	private String prescriptionName;
	@XmlElement(name = "prescribe_time")
	private String prescribeTime;
	@XmlElement(name = "creator_name")
	private String creatorName;
	@XmlElement(name = "creation_time")
	private String creationTime;
	@XmlElement(name = "valueSn")
	private String valueSn;
	@XmlElement(name = "valuer_name")
	private String valuerName;
	@XmlElement(name = "valuation_time")
	private String valuationTime;
	@XmlElement(name = "price")
	private String price;
	@XmlElement(name = "quantity")
	private String quantity;
	@XmlElement(name = "quantity_day")
	private String quantityDay;
	@XmlElement(name = "price_total")
	private String priceTotal;
	@XmlElement(name = "date_de")
	private String dateDe;
	@XmlElement(name = "eu_herbtype")
	private String euHerbtype;
	@XmlElement(name = "payment_type")
	private String paymentType;
	@XmlElement(name = "payment_status")
	private String paymentStatus;
	@XmlElement(name = "device_id")
	private String deviceId;
	@XmlElement(name = "process_status")
	private String processStatus;
	@XmlElement(name = "description")
	private String description;
	@XmlElement(name = "patient_type")
	private String patientType;
	@XmlElement(name = "payments")
	private String payments;
	@XmlElement(name = "dispensary")
	private String dispensary;
	@XmlElement(name = "pv_carno")
	private String pvCarno;
	@XmlElement(name = "invoice_no")
	private String invoiceNo;
	@XmlElement(name = "presc_identity")
	private String prescIdentity;
	@XmlElement(name = "presc_attr")
	private String prescAttr;
	@XmlElement(name = "dispense_pri")
	private String dispensePri;
	@XmlElement(name = "repetition")
	private String repetition;
	@XmlElement(name = "code_charge_type")
	private String codeChargeType;
	@XmlElement(name = "name_charge_type")
	private String nameChargeType;
	@XmlElement(name = "sex")
	private String sex;
	@XmlElement(name = "rcpt_info")
	private String rcptInfo;
	@XmlElement(name = "costs")
	private String costs;
	@XmlElement(name = "date_of_birth")
	private String dateOfBirth;
	@XmlElement(name = "data_prescription_detail")
	private List<DataPrescriptionDetail> dataPrescriptionDetail;
	
	
	public String getPkPresocc() {
		return pkPresocc;
	}
	public void setPkPresocc(String pkPresocc) {
		this.pkPresocc = pkPresocc;
	}
	public String getAutoid() {
		return autoid;
	}
	public void setAutoid(String autoid) {
		this.autoid = autoid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRegisterId() {
		return registerId;
	}
	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}
	public String getCodePvtype() {
		return codePvtype;
	}
	public void setCodePvtype(String codePvtype) {
		this.codePvtype = codePvtype;
	}
	public String getCodePati() {
		return codePati;
	}
	public void setCodePati(String codePati) {
		this.codePati = codePati;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getTele() {
		return tele;
	}
	public void setTele(String tele) {
		this.tele = tele;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getPrescriptionName() {
		return prescriptionName;
	}
	public void setPrescriptionName(String prescriptionName) {
		this.prescriptionName = prescriptionName;
	}
	public String getPrescribeTime() {
		return prescribeTime;
	}
	public void setPrescribeTime(String prescribeTime) {
		this.prescribeTime = prescribeTime;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public String getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}
	public String getValueSn() {
		return valueSn;
	}
	public void setValueSn(String valueSn) {
		this.valueSn = valueSn;
	}
	public String getValuerName() {
		return valuerName;
	}
	public void setValuerName(String valuerName) {
		this.valuerName = valuerName;
	}
	public String getValuationTime() {
		return valuationTime;
	}
	public void setValuationTime(String valuationTime) {
		this.valuationTime = valuationTime;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getQuantityDay() {
		return quantityDay;
	}
	public void setQuantityDay(String quantityDay) {
		this.quantityDay = quantityDay;
	}
	public String getPriceTotal() {
		return priceTotal;
	}
	public void setPriceTotal(String priceTotal) {
		this.priceTotal = priceTotal;
	}
	public String getDateDe() {
		return dateDe;
	}
	public void setDateDe(String dateDe) {
		this.dateDe = dateDe;
	}
	public String getEuHerbtype() {
		return euHerbtype;
	}
	public void setEuHerbtype(String euHerbtype) {
		this.euHerbtype = euHerbtype;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPatientType() {
		return patientType;
	}
	public void setPatientType(String patientType) {
		this.patientType = patientType;
	}
	public String getPayments() {
		return payments;
	}
	public void setPayments(String payments) {
		this.payments = payments;
	}
	public String getDispensary() {
		return dispensary;
	}
	public void setDispensary(String dispensary) {
		this.dispensary = dispensary;
	}
	public String getPvCarno() {
		return pvCarno;
	}
	public void setPvCarno(String pvCarno) {
		this.pvCarno = pvCarno;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getPrescIdentity() {
		return prescIdentity;
	}
	public void setPrescIdentity(String prescIdentity) {
		this.prescIdentity = prescIdentity;
	}
	public String getPrescAttr() {
		return prescAttr;
	}
	public void setPrescAttr(String prescAttr) {
		this.prescAttr = prescAttr;
	}
	public String getDispensePri() {
		return dispensePri;
	}
	public void setDispensePri(String dispensePri) {
		this.dispensePri = dispensePri;
	}
	public String getRepetition() {
		return repetition;
	}
	public void setRepetition(String repetition) {
		this.repetition = repetition;
	}
	public String getCodeChargeType() {
		return codeChargeType;
	}
	public void setCodeChargeType(String codeChargeType) {
		this.codeChargeType = codeChargeType;
	}
	public String getNameChargeType() {
		return nameChargeType;
	}
	public void setNameChargeType(String nameChargeType) {
		this.nameChargeType = nameChargeType;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getRcptInfo() {
		return rcptInfo;
	}
	public void setRcptInfo(String rcptInfo) {
		this.rcptInfo = rcptInfo;
	}
	public String getCosts() {
		return costs;
	}
	public void setCosts(String costs) {
		this.costs = costs;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public List<DataPrescriptionDetail> getDataPrescriptionDetail() {
		if(dataPrescriptionDetail==null)dataPrescriptionDetail=new ArrayList<DataPrescriptionDetail>();
		return dataPrescriptionDetail;
	}
	public void setDataPrescriptionDetail(
			List<DataPrescriptionDetail> dataPrescriptionDetail) {
		this.dataPrescriptionDetail = dataPrescriptionDetail;
	}
	
	
}
