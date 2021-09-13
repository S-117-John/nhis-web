package com.zebone.nhis.webservice.lbzy.model;

import javax.xml.bind.annotation.*;
import java.util.Set;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryToPayItem {

    @XmlElement(name = "RegFlow")
    private String regFlow;
    @XmlElement(name = "PatientID")
    private String patientID;
    @XmlElement(name = "DepartmentID")
    private String departmentID;
    @XmlElement(name = "DepartmentName")
    private String departmentName;
    @XmlElement(name = "DoctorID")
    private String doctorID;
    @XmlElement(name = "DoctorName")
    private String doctorName;
    @XmlElement(name = "RecipeNO")
    private String recipeNO;
    @XmlElement(name = "RecipeSEQ")
    private String recipeSEQ;
    @XmlElement(name = "SeeTime")
    private String seeTime;
    @XmlElement(name = "TotalCost")
    private Double totalCost;

    @XmlTransient
    private Set<String> recipeSEQS;

    public String getRegFlow() {
        return regFlow;
    }

    public void setRegFlow(String regFlow) {
        this.regFlow = regFlow;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(String departmentID) {
        this.departmentID = departmentID;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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

    public String getRecipeNO() {
        return recipeNO;
    }

    public void setRecipeNO(String recipeNO) {
        this.recipeNO = recipeNO;
    }

    public String getRecipeSEQ() {
        return recipeSEQ;
    }

    public void setRecipeSEQ(String recipeSEQ) {
        this.recipeSEQ = recipeSEQ;
    }

    public String getSeeTime() {
        return seeTime;
    }

    public void setSeeTime(String seeTime) {
        this.seeTime = seeTime;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Set<String> getRecipeSEQS() {
        return recipeSEQS;
    }

    public void setRecipeSEQS(Set<String> recipeSEQS) {
        this.recipeSEQS = recipeSEQS;
    }
}
