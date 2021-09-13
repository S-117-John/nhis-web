package com.zebone.nhis.ma.pub.zsrm.vo;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "Prescriberecord")
@XmlAccessorType(XmlAccessType.NONE)
public class ZsrmHerbPresVo {

    @XmlElement(name = "Id")
    private String presNo;

    @XmlElement(name = "RegisterId")
    private String codePv;

    @XmlElement(name = "Name")
    private String namePi;

    @XmlElement(name = "Sex")
    private String dtSex;

    @XmlElement(name = "Age")
    private String age;

    @XmlElement(name = "Tele")
    private String mobile;

    @XmlElement(name = "Email")
    private String email;

    @XmlElement(name = "DepartmentName")
    private String nameDeptPres;

    @XmlElement(name = "DoctorName")
    private String nameEmpPres;

    @XmlElement(name = "PrescriptionName")
    private String nameOrd;

    @XmlElement(name = "PrescribeTime")
    private String datePres;

    @XmlElement(name = "CreatorName")
    private String nameEmpInp;

    @XmlElement(name = "CreationTime")
    private String dateInp;

    @XmlElement(name = "ValueSn")
    private String codeSt;

    @XmlElement(name = "ValuerName")
    private String nameEmpSt;

    @XmlElement(name = "ValuationTime")
    private String dateSt;

    @XmlElement(name = "Price")
    private double price;


    @XmlElement(name = "Quantity")
    private String ords;

    @XmlElement(name = "QuantityDay")
    private String ordsDay;

    @XmlElement(name = "PriceTotal")
    private double priceTotal;

    @XmlElement(name = "PaymentType")
    private String hpName;

    @XmlElement(name = "PaymentStatus")
    private String paymentStatus;

    @XmlElement(name = "DataSource")
    private String dataSource;

    @XmlElement(name = "DeviceId")
    private String deviceId;

    @XmlElement(name = "ProcessStatus")
    private String processStatus;

    @XmlElement(name = "Description")
    private String noteOrd;

    private String codeStore;

    @XmlElementWrapper(name = "Prescribedetails")
    @XmlElement(name = "Prescribedetail")
    private List<ZsrmHerbPresDtVo> dtVoList;


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

    public String getCodeStore() {
        return codeStore;
    }

    public void setCodeStore(String codeStore) {
        this.codeStore = codeStore;
    }

    public List<ZsrmHerbPresDtVo> getDtVoList() {
        return dtVoList;
    }

    public void setDtVoList(List<ZsrmHerbPresDtVo> dtVoList) {
        this.dtVoList = dtVoList;
    }
}
