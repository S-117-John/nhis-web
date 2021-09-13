package com.zebone.nhis.webservice.lbzy.model.reg;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class RegApptVo {
    @XmlElement(name = "ResultCode")
    private String resultCode;

    @XmlElement(name = "ErrorMsg")
    private String errorMsg;

    @XmlElement(name = "RegFlow")
    private String regFlow;

    @XmlElement(name = "DoctorID")
    private String doctorID;

    @XmlElement(name = "SeeTime")
    private String seeTime;

    @XmlElement(name = "SeeNO")
    private String seeNO;

    @XmlElement(name = "PatientName")
    private String patientName;

    @XmlElement(name = "PatientID")
    private String patientID;

    @XmlElement(name = "DepartmentAddress")
    private String departmentAddress;

    @XmlElement(name = "DoctorName")
    private String doctorName;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getRegFlow() {
        return regFlow;
    }

    public void setRegFlow(String regFlow) {
        this.regFlow = regFlow;
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

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
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
}
