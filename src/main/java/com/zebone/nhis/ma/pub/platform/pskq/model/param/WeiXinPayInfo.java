package com.zebone.nhis.ma.pub.platform.pskq.model.param;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "WeiXinPayInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class WeiXinPayInfo {

    @XmlElement(name = "UserType")
    private String userType;

    @XmlElement(name = "HospitalId")
    private String hospitalId;

    @XmlElement(name = "PatientId")
    private String patientId;

    @XmlElement(name = "CardNo")
    private String cardNo;

    @XmlElement(name = "Name")
    private String name;

    @XmlElement(name = "ClinicID")
    private String clinicID;

    @XmlElement(name = "OrderType")
    private String orderType;

    @XmlElement(name = "OrderDept")
    private String orderDept;

    @XmlElement(name = "OrderDoctor")
    private String orderDoctor;

    @XmlElement(name = "OrderDate")
    private String orderDate;

    @XmlElement(name = "RecipeSeq")
    private String recipeSeq;

    @XmlElement(name = "PactCode")
    private String pactCode;

    @XmlElement(name = "SumMoney")
    private String sumMoney;

    @XmlElement(name = "YiBaoMoney")
    private String yiBaoMoney;

    @XmlElement(name = "PrescMoney")
    private String prescMoney;
    
    @XmlElement(name = "IdenNo")
    private String idenNo;

    public String getRecipeSeq() {
        return recipeSeq;
    }

    public void setRecipeSeq(String recipeSeq) {
        this.recipeSeq = recipeSeq;
    }

    public String getPactCode() {
        return pactCode;
    }

    public void setPactCode(String pactCode) {
        this.pactCode = pactCode;
    }

    public String getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(String sumMoney) {
        this.sumMoney = sumMoney;
    }

    public String getYiBaoMoney() {
        return yiBaoMoney;
    }

    public void setYiBaoMoney(String yiBaoMoney) {
        this.yiBaoMoney = yiBaoMoney;
    }

    public String getPrescMoney() {
        return prescMoney;
    }

    public void setPrescMoney(String prescMoney) {
        this.prescMoney = prescMoney;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClinicID() {
        return clinicID;
    }

    public void setClinicID(String clinicID) {
        this.clinicID = clinicID;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderDept() {
        return orderDept;
    }

    public void setOrderDept(String orderDept) {
        this.orderDept = orderDept;
    }

    public String getOrderDoctor() {
        return orderDoctor;
    }

    public void setOrderDoctor(String orderDoctor) {
        this.orderDoctor = orderDoctor;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

	public String getIdenNo() {
		return idenNo;
	}

	public void setIdenNo(String idenNo) {
		this.idenNo = idenNo;
	} 
}
