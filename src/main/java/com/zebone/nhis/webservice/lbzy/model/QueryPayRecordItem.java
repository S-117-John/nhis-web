package com.zebone.nhis.webservice.lbzy.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryPayRecordItem {

    @XmlElement(name = "InvoiceNO")
    private String invoiceNO;

    @XmlElement(name = "TotalCost")
    private String totalCost;

    @XmlElement(name = "PayTypeName")
    private String payTypeName;

    @XmlElement(name = "PayDate")
    private String payDate;

    @XmlElement(name = "PactName")
    private String pactName;

    @XmlElement(name = "RegisterNO")
    private String registerNO;

    @XmlElement(name = "DepartmentName")
    private String departmentName;

    @XmlElement(name = "DoctorName")
    private String doctorName;


    public String getInvoiceNO() {
        return invoiceNO;
    }

    public void setInvoiceNO(String invoiceNO) {
        this.invoiceNO = invoiceNO;
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

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getPactName() {
        return pactName;
    }

    public void setPactName(String pactName) {
        this.pactName = pactName;
    }

    public String getRegisterNO() {
        return registerNO;
    }

    public void setRegisterNO(String registerNO) {
        this.registerNO = registerNO;
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
}
