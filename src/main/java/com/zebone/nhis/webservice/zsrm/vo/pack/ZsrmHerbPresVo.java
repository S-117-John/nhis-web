package com.zebone.nhis.webservice.zsrm.vo.pack;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "PRESCRIBERECORD")
@XmlAccessorType(XmlAccessType.FIELD)
public class ZsrmHerbPresVo {

    @XmlElement(name = "SEQUENCE_NO")
    private String no;

    @XmlElement(name = "PRESCRIBE_NO")
    private String presNo;

    @XmlElement(name = "VISIT_NO")
    private String codePv;

    @XmlElement(name = "PATIENT_NAME")
    private String namePi;

    @XmlElement(name = "PATIENT_SEX")
    private String dtSex;

    @XmlElement(name = "PATIENT_AGE")
    private String age;

    @XmlElement(name = "CONTACT_TEL_NO")
    private String mobile;

    @XmlElement(name = "PATIENT_EMAIL")
    private String email;

    @XmlElement(name = "APPLY_DEPT")
    private String nameDeptPres;

    @XmlElement(name = "APPLY_OPERATOR")
    private String nameEmpPres;

    @XmlElement(name = "PRESCRIPTION_NAME")
    private String nameOrd;

    @XmlElement(name = "APPLY_TIME")
    private String datePres;

    @XmlElement(name = "INPUT_OPERATOR")
    private String nameEmpInp;

    @XmlElement(name = "INPUT_TIME")
    private String dateInp;

    @XmlElement(name = "FEE_BILL_NO")
    private String codeSt;

    @XmlElement(name = "PERFORM_OPERATOR")
    private String nameEmpSt;

    @XmlElement(name = "FEE_TIME")
    private String dateSt;

    @XmlElement(name = "UNIT_PRICE")
    private double price;


    @XmlElement(name = "QUANTITY")
    private String ords;

    @XmlElement(name = "QUANTITY_DAY")
    private String ordsDay;

    @XmlElement(name = "COST")
    private double priceTotal;

    @XmlElement(name = "RATE_TYPE")
    private String hpName;

    @XmlElement(name = "CHARGE_STATUS")
    private String paymentStatus;

    @XmlElement(name = "PRESCRIBE_SOURCE")
    private String dataSource;

    @XmlElement(name = "DEVICE_DOSING")
    private String deviceId;

    @XmlElement(name = "ADJUST_STATUS")
    private String processStatus;

    @XmlElement(name = "REMARK")
    private String noteOrd;



    public String getPresNo() {
        return presNo;
    }

    public void setPresNo(String presNo) {
        this.presNo = presNo;
    }

    public String getCodePv() {
        return codePv;
    }

    public void setCodePv(String codePv) {
        this.codePv = codePv;
    }

    public String getNamePi() {
        return namePi;
    }

    public void setNamePi(String namePi) {
        this.namePi = namePi;
    }

    public String getDtSex() {
        return dtSex;
    }

    public void setDtSex(String dtSex) {
        this.dtSex = dtSex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNameDeptPres() {
        return nameDeptPres;
    }

    public void setNameDeptPres(String nameDeptPres) {
        this.nameDeptPres = nameDeptPres;
    }

    public String getNameEmpPres() {
        return nameEmpPres;
    }

    public void setNameEmpPres(String nameEmpPres) {
        this.nameEmpPres = nameEmpPres;
    }

    public String getNameOrd() {
        return nameOrd;
    }

    public void setNameOrd(String nameOrd) {
        this.nameOrd = nameOrd;
    }

    public String getDatePres() {
        return datePres;
    }

    public void setDatePres(String datePres) {
        this.datePres = datePres;
    }

    public String getNameEmpInp() {
        return nameEmpInp;
    }

    public void setNameEmpInp(String nameEmpInp) {
        this.nameEmpInp = nameEmpInp;
    }

    public String getDateInp() {
        return dateInp;
    }

    public void setDateInp(String dateInp) {
        this.dateInp = dateInp;
    }

    public String getCodeSt() {
        return codeSt;
    }

    public void setCodeSt(String codeSt) {
        this.codeSt = codeSt;
    }

    public String getNameEmpSt() {
        return nameEmpSt;
    }

    public void setNameEmpSt(String nameEmpSt) {
        this.nameEmpSt = nameEmpSt;
    }

    public String getDateSt() {
        return dateSt;
    }

    public void setDateSt(String dateSt) {
        this.dateSt = dateSt;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getOrds() {
        return ords;
    }

    public void setOrds(String ords) {
        this.ords = ords;
    }

    public String getOrdsDay() {
        return ordsDay;
    }

    public void setOrdsDay(String ordsDay) {
        this.ordsDay = ordsDay;
    }

    public double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public String getHpName() {
        return hpName;
    }

    public void setHpName(String hpName) {
        this.hpName = hpName;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
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

    public String getNoteOrd() {
        return noteOrd;
    }

    public void setNoteOrd(String noteOrd) {
        this.noteOrd = noteOrd;
    }

}
