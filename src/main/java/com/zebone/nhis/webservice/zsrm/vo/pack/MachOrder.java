package com.zebone.nhis.webservice.zsrm.vo.pack;

import com.zebone.nhis.webservice.zsrm.support.JaxbDateAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@XmlAccessorType(value = XmlAccessType.FIELD)
public class MachOrder {

    @XmlRootElement(name="DocumentElement")
    @XmlAccessorType(value = XmlAccessType.FIELD)
    public static class MachOrderHeader {

        @XmlElement(name = "DataTable")
        private List<MachOrder> orders;

        public List<MachOrder> getOrders() {
            return orders;
        }

        public void setOrders(List<MachOrder> orders) {
            this.orders = orders;
        }
    }

    @XmlElement(name = "OrderID")
    private String orderId;
    @XmlElement(name = "PatientID")
    private String patientId;
    @XmlElement(name = "PatientName")
    private String patientName;
    @XmlElement(name = "PatientGender")
    private String patientGender;
    @XmlElement(name = "address")
    private String address;
    @XmlElement(name = "Tel1")
    private String tel1;
    @XmlElement(name = "Tel2")
    private String tel2;
    @XmlElement(name = "Doctor_Name")
    private String doctorName;
    @XmlElement(name = "Primary_Diagnosis")
    private String primaryDiagnosis;
    @XmlElement(name = "Secondary_Diagnosis")
    private String secondaryDiagnosis;
    @XmlElement(name = "Patient_Age")
    private String patientAge;
    @XmlElement(name = "No_Of_Visit")
    private String noOfVisit;
    @XmlElement(name = "Payment_No")
    private String paymentNo;
    @XmlElement(name = "Current_No")
    private String currentNo;
    @XmlElement(name = "Ward")
    private String ward;
    @XmlElement(name = "Cashier_Name")
    private String cashierName;
    @XmlElement(name = "Total_Payment")
    private BigDecimal totalPayment;
    @XmlElement(name = "DateTime_Of_Payment")
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date dateTimeOfPayment;
    @XmlElement(name = "DateTime_Of_Printing")
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date dateTimeOfPrinting;
    @XmlElement(name = "Drug_Flag")
    private String drugFlag;

    @XmlTransient
    private String pkPresocc;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPrimaryDiagnosis() {
        return primaryDiagnosis;
    }

    public void setPrimaryDiagnosis(String primaryDiagnosis) {
        this.primaryDiagnosis = primaryDiagnosis;
    }

    public String getSecondaryDiagnosis() {
        return secondaryDiagnosis;
    }

    public void setSecondaryDiagnosis(String secondaryDiagnosis) {
        this.secondaryDiagnosis = secondaryDiagnosis;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getNoOfVisit() {
        return noOfVisit;
    }

    public void setNoOfVisit(String noOfVisit) {
        this.noOfVisit = noOfVisit;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public String getCurrentNo() {
        return currentNo;
    }

    public void setCurrentNo(String currentNo) {
        this.currentNo = currentNo;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public Date getDateTimeOfPayment() {
        return dateTimeOfPayment;
    }

    public void setDateTimeOfPayment(Date dateTimeOfPayment) {
        this.dateTimeOfPayment = dateTimeOfPayment;
    }

    public Date getDateTimeOfPrinting() {
        return dateTimeOfPrinting;
    }

    public void setDateTimeOfPrinting(Date dateTimeOfPrinting) {
        this.dateTimeOfPrinting = dateTimeOfPrinting;
    }

    public String getDrugFlag() {
        return drugFlag;
    }

    public void setDrugFlag(String drugFlag) {
        this.drugFlag = drugFlag;
    }


    public String getPkPresocc() {
        return pkPresocc;
    }

    public void setPkPresocc(String pkPresocc) {
        this.pkPresocc = pkPresocc;
    }

}
