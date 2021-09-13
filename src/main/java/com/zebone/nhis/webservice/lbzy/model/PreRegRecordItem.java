package com.zebone.nhis.webservice.lbzy.model;

import javax.xml.bind.annotation.*;

/**
 * @author 卡卡西
 */
@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class PreRegRecordItem {

    @XmlTransient
    private String pkSchsrv;
    @XmlTransient
    private String pkSchres;
    @XmlElement(name = "DepartmentName")
    private String departmentName;
    @XmlElement(name = "ScheduleID")
    private String scheduleID;
    @XmlElement(name = "BookDate")
    private String bookDate;
    @XmlElement(name = "Noon")
    private String noon;
    @XmlElement(name = "DoctorName")
    private String doctorName;
    @XmlElement(name = "ReglevelName")
    private String regLevelName;
    @XmlElement(name = "RegCost")
    private Double regCost;
    @XmlElement(name = "DiagCost")
    private Double diagCost;
    @XmlElement(name = "OtherCost")
    private Double otherCost;
    @XmlElement(name = "ClinicTime")
    private String clinicTime;
    @XmlElement(name = "RegFlow")
    private String regFlow;

    public String getPkSchsrv() {
        return pkSchsrv;
    }

    public void setPkSchsrv(String pkSchsrv) {
        this.pkSchsrv = pkSchsrv;
    }

    public String getPkSchres() {
        return pkSchres;
    }

    public void setPkSchres(String pkSchres) {
        this.pkSchres = pkSchres;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(String scheduleID) {
        this.scheduleID = scheduleID;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

    public String getNoon() {
        return noon;
    }

    public void setNoon(String noon) {
        this.noon = noon;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getRegLevelName() {
        return regLevelName;
    }

    public void setRegLevelName(String regLevelName) {
        this.regLevelName = regLevelName;
    }

    public Double getRegCost() {
        return regCost;
    }

    public void setRegCost(Double regCost) {
        this.regCost = regCost;
    }

    public Double getDiagCost() {
        return diagCost;
    }

    public void setDiagCost(Double diagCost) {
        this.diagCost = diagCost;
    }

    public Double getOtherCost() {
        return otherCost;
    }

    public void setOtherCost(Double otherCost) {
        this.otherCost = otherCost;
    }

    public String getClinicTime() {
        return clinicTime;
    }

    public void setClinicTime(String clinicTime) {
        this.clinicTime = clinicTime;
    }

    public String getRegFlow() {
        return regFlow;
    }

    public void setRegFlow(String regFlow) {
        this.regFlow = regFlow;
    }
}
