package com.zebone.nhis.webservice.lbzy.model.ipin;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryInHospital {

    @XmlTransient
    private String pkPv;

    @XmlElement(name = "ResultCode")
    private String resultCode;

    @XmlElement(name = "ErrorMsg")
    private String errorMsg;

    @XmlElement(name = "InPatientNO")
    private String inPatientNO;

    @XmlElement(name = "InPatientID")
    private String inPatientID;

    @XmlElement(name = "PatientName")
    private String patientName;

    @XmlElement(name = "PatientSexID")
    private String patientSexID;

    @XmlElement(name = "Birthday")
    private String birthday;

    @XmlElement(name = "PatientAge")
    private String patientAge;

    @XmlElement(name = "InDate")
    private String inDate;

    @XmlElement(name = "BedNO")
    private String bedNO;

    @XmlElement(name = "DepartmentCode")
    private String departmentCode;

    @XmlElement(name = "DepartmentName")
    private String departmentName;

    @XmlElement(name = "TotalCost")
    private Double totalCost;

    @XmlElement(name = "PubCost")
    private Double pubCost;

    @XmlElement(name = "Paycost")
    private Double payCost;

    @XmlElement(name = "OwnCost")
    private Double ownCost;

    @XmlElement(name = "PrePayBalance")
    private Double prePayBalance;

    @XmlElement(name = "IDCardNO")
    private String idCardNo;

    @XmlElement(name = "PactCode")
    private String pactCode;

    @XmlElement(name = "PactName")
    private String pactName;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getInPatientNO() {
        return inPatientNO;
    }

    public void setInPatientNO(String inPatientNO) {
        this.inPatientNO = inPatientNO;
    }

    public String getInPatientID() {
        return inPatientID;
    }

    public void setInPatientID(String inPatientID) {
        this.inPatientID = inPatientID;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        this.inDate = inDate;
    }

    public String getBedNO() {
        return bedNO;
    }

    public void setBedNO(String bedNO) {
        this.bedNO = bedNO;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Double getPubCost() {
        return pubCost;
    }

    public void setPubCost(Double pubCost) {
        this.pubCost = pubCost;
    }

    public Double getPayCost() {
        return payCost;
    }

    public void setPayCost(Double payCost) {
        this.payCost = payCost;
    }

    public Double getOwnCost() {
        return ownCost;
    }

    public void setOwnCost(Double ownCost) {
        this.ownCost = ownCost;
    }

    public Double getPrePayBalance() {
        return prePayBalance;
    }

    public void setPrePayBalance(Double prePayBalance) {
        this.prePayBalance = prePayBalance;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getPactCode() {
        return pactCode;
    }

    public void setPactCode(String pactCode) {
        this.pactCode = pactCode;
    }

    public String getPactName() {
        return pactName;
    }

    public void setPactName(String pactName) {
        this.pactName = pactName;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }
}
