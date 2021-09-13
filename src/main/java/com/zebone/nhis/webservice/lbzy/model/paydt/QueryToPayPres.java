package com.zebone.nhis.webservice.lbzy.model.paydt;

import javax.xml.bind.annotation.*;
import java.util.Set;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryToPayPres {

    @XmlElement(name = "RegFlow")
    private String regFlow;
    @XmlElement(name = "PatientName")
    private String patientName;
    @XmlElement(name = "PatientSexID")
    private String patientSexID;
    @XmlElement(name = "Birthday")
    private String birthday;
    @XmlElement(name = "RecipeSEQ")
    private String recipeSEQ;
    @XmlElement(name = "DoctorID")
    private String doctorID;
    @XmlElement(name = "DoctorName")
    private String doctorName;
    @XmlElement(name = "DeptID")
    private String deptID;
    @XmlElement(name = "DeptName")
    private String deptName;
    @XmlElement(name = "SeeID")
    private String seeID;
    @XmlElement(name = "IDCardNO")
    private String idCardNO;
    @XmlElement(name = "TEL")
    private String tel;
    @XmlElement(name = "PresDate")
    private String presDate;
    @XmlElement(name = "medicalType")
    private String medicalType;
    @XmlElement(name = "Diagnosis")
    private String diagnosis;
    @XmlElement(name = "Fee")
    private Double fee;
    @XmlElement(name = "RecipeDescribe")
    private String recipeDescribe;
    @XmlTransient
    private Set<String> recipeSEQS;

    public String getRegFlow() {
        return regFlow;
    }

    public void setRegFlow(String regFlow) {
        this.regFlow = regFlow;
    }

    public Set<String> getRecipeSEQS() {
        return recipeSEQS;
    }

    public void setRecipeSEQS(Set<String> recipeSEQS) {
        this.recipeSEQS = recipeSEQS;
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

    public String getRecipeSEQ() {
        return recipeSEQ;
    }

    public void setRecipeSEQ(String recipeSEQ) {
        this.recipeSEQ = recipeSEQ;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDeptID() {
        return deptID;
    }

    public void setDeptID(String deptID) {
        this.deptID = deptID;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getSeeID() {
        return seeID;
    }

    public void setSeeID(String seeID) {
        this.seeID = seeID;
    }

    public String getIdCardNO() {
        return idCardNO;
    }

    public void setIdCardNO(String idCardNO) {
        this.idCardNO = idCardNO;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPresDate() {
        return presDate;
    }

    public void setPresDate(String presDate) {
        this.presDate = presDate;
    }

    public String getMedicalType() {
        return medicalType;
    }

    public void setMedicalType(String medicalType) {
        this.medicalType = medicalType;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getRecipeDescribe() {
        return recipeDescribe;
    }

    public void setRecipeDescribe(String recipeDescribe) {
        this.recipeDescribe = recipeDescribe;
    }
}
