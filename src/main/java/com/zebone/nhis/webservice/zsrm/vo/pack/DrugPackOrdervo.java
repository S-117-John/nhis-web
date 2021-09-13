package com.zebone.nhis.webservice.zsrm.vo.pack;



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DataTable")
@XmlAccessorType(XmlAccessType.NONE)
public class DrugPackOrdervo {

    @XmlElement(name = "OrderID")
    private String presNo;

    @XmlElement(name = "PatientID")
    private String codePi;

    @XmlElement(name = "PatientName")
    private String namePi;

    @XmlElement(name = "PatientGender")
    private String sex;

    @XmlElement(name = "Doctor_Name")
    private String nameEmpPres;

    @XmlElement(name = "Primary_Diagnosis")
    private String diagName;


    @XmlElement(name = "Patient_Age")
    private String age;

    @XmlElement(name = "Address")
    private String address;

    @XmlElement(name = "Tel1")
    private String telNo;

    @XmlElement(name = "Tel2")
    private String modile;

    @XmlElement(name = "No_Of_Visi")
    private String visit;

    @XmlElement(name = "Payment_No")
    private String codeSt;

    @XmlElement(name = "Current_No")
    private String invNo;

    @XmlElement(name = "Ward")
    private String deptName;

    @XmlElement(name = "Cashier_Name")
    private String nameEmpSt;

    @XmlElement(name = "Total_Payment")
    private String amountSt;

    @XmlElement(name = "DateTime_Of_Payment")
    private String dateSt;

    public String getPresNo() {
        return presNo;
    }

    public void setPresNo(String presNo) {
        this.presNo = presNo;
    }

    public String getCodePi() {
        return codePi;
    }

    public void setCodePi(String codePi) {
        this.codePi = codePi;
    }

    public String getNamePi() {
        return namePi;
    }

    public void setNamePi(String namePi) {
        this.namePi = namePi;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNameEmpPres() {
        return nameEmpPres;
    }

    public void setNameEmpPres(String nameEmpPres) {
        this.nameEmpPres = nameEmpPres;
    }

    public String getDiagName() {
        return diagName;
    }

    public void setDiagName(String diagName) {
        this.diagName = diagName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getModile() {
        return modile;
    }

    public void setModile(String modile) {
        this.modile = modile;
    }

    public String getVisit() {
        return visit;
    }

    public void setVisit(String visit) {
        this.visit = visit;
    }

    public String getCodeSt() {
        return codeSt;
    }

    public void setCodeSt(String codeSt) {
        this.codeSt = codeSt;
    }

    public String getInvNo() {
        return invNo;
    }

    public void setInvNo(String invNo) {
        this.invNo = invNo;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getNameEmpSt() {
        return nameEmpSt;
    }

    public void setNameEmpSt(String nameEmpSt) {
        this.nameEmpSt = nameEmpSt;
    }

    public String getAmountSt() {
        return amountSt;
    }

    public void setAmountSt(String amountSt) {
        this.amountSt = amountSt;
    }

    public String getDateSt() {
        return dateSt;
    }

    public void setDateSt(String dateSt) {
        this.dateSt = dateSt;
    }
}
