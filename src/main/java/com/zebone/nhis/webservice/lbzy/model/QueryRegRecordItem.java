package com.zebone.nhis.webservice.lbzy.model;



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)

public class QueryRegRecordItem {

    @XmlElement(name = "RegFlow")
    private String regFlow;
    @XmlElement(name = "RegType")
    private String regType;
    @XmlElement(name = "DoctorID")
    private String doctorID;
    @XmlElement(name = "SeeTime")
    private String seeTime;
    @XmlElement(name = "RegTime")
    private String regTime;
    @XmlElement(name = "SeeNO")
    private String seeNO;
    @XmlElement(name = "PatientName")
    private String patientName;
    @XmlElement(name = "PatientSexID")
    private String patientSexID;
    @XmlElement(name = "PatientAge")
    private String patientAge;
    @XmlElement(name = "PatientID")
    private String patientID;
    @XmlElement(name = "FeeType")
    private String feeType;
    @XmlElement(name = "FeeItem")
    private String feeItem;
    @XmlElement(name = "DepartmentName")
    private String departmentName;
    @XmlElement(name = "DepartmentAddress")
    private String departmentAddress;
    @XmlElement(name = "DoctorName")
    private String doctorName;
    @XmlElement(name = "TotalCost")
    private String totalCost;
    @XmlElement(name = "PayTypeName")
    private String payTypeName;

    public String getRegFlow() {
        return regFlow;
    }

    public void setRegFlow(String regFlow) {
        this.regFlow = regFlow;
    }

    public String getRegType() {
        return regType;
    }

    public void setRegType(String regType) {
        this.regType = regType;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getSeeTime() {
        return seeTime;
    }

    public void setSeeTime(String seeTime) {
        this.seeTime = seeTime;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getSeeNO() {
        return seeNO;
    }

    public void setSeeNO(String seeNO) {
        this.seeNO = seeNO;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientSexID() {
        return patientSexID;
    }

    public void setPatientSexID(String patientSexID) {
        this.patientSexID = patientSexID;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getFeeItem() {
        return feeItem;
    }

    public void setFeeItem(String feeItem) {
        this.feeItem = feeItem;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentAddress() {
        return departmentAddress;
    }

    public void setDepartmentAddress(String departmentAddress) {
        this.departmentAddress = departmentAddress;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }
}
