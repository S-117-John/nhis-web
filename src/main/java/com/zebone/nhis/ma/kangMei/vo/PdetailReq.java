package com.zebone.nhis.ma.kangMei.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "", propOrder = {"userName","age","gender","tel","isSuffering","sufferingNum","amount","isPregnant","money","jiFried","type","isWithin","otherPresNum",
		"specialInstru","bedNum","hosDepart","hospitalNum","diseaseCode","doctor","prescriptRemark","isHos","medicationMethods","medicationInstruction","checkArea","xqReq"
})
@XmlRootElement(name = "PdetailReq")
public class PdetailReq {
	private String userName;

	private String age;

	private String gender;

	private String tel;

	private String isSuffering;

	private String sufferingNum;

	private String amount;

	private String isPregnant;

	private String money;

	private String jiFried;

	private String type;

	private String isWithin;

	private String otherPresNum;

	private String specialInstru;

	private String bedNum;

	private String hosDepart;

	private String hospitalNum;

	private String diseaseCode;

	private String doctor;

	private String prescriptRemark;

	private String isHos;
	
	private String medicationMethods;
	
	private String medicationInstruction;
	private String checkArea;
	private List<XqReq> xqReq;
	@XmlElement(name = "check_area")
	public String getCheckArea() {
		return checkArea;
	}

	public void setCheckArea(String checkArea) {
		this.checkArea = checkArea;
	}
	
	@XmlElement(name = "medication_methods")
	public String getMedicationMethods() {
		return medicationMethods;
	}

	public void setMedicationMethods(String medicationMethods) {
		this.medicationMethods = medicationMethods;
	}

	@XmlElement(name = "medication_instruction")
	public String getMedicationInstruction() {
		return medicationInstruction;
	}

	public void setMedicationInstruction(String medicationInstruction) {
		this.medicationInstruction = medicationInstruction;
	}

	@XmlElement(name = "user_name")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@XmlElement(name = "age")
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	@XmlElement(name = "gender")
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@XmlElement(name = "tel")
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@XmlElement(name = "is_suffering")
	public String getIsSuffering() {
		return isSuffering;
	}

	public void setIsSuffering(String isSuffering) {
		this.isSuffering = isSuffering;
	}

	@XmlElement(name = "suffering_num")
	public String getSufferingNum() {
		return sufferingNum;
	}

	public void setSufferingNum(String sufferingNum) {
		this.sufferingNum = sufferingNum;
	}

	@XmlElement(name = "amount")
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@XmlElement(name = "is_pregnant")
	public String getIsPregnant() {
		return isPregnant;
	}

	public void setIsPregnant(String isPregnant) {
		this.isPregnant = isPregnant;
	}

	@XmlElement(name = "money")
	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	@XmlElement(name = "ji_fried")
	public String getJiFried() {
		return jiFried;
	}

	public void setJiFried(String jiFried) {
		this.jiFried = jiFried;
	}

	@XmlElement(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlElement(name = "is_within")
	public String getIsWithin() {
		return isWithin;
	}

	public void setIsWithin(String isWithin) {
		this.isWithin = isWithin;
	}

	@XmlElement(name = "other_pres_num")
	public String getOtherPresNum() {
		return otherPresNum;
	}

	public void setOtherPresNum(String otherPresNum) {
		this.otherPresNum = otherPresNum;
	}

	@XmlElement(name = "special_instru")
	public String getSpecialInstru() {
		return specialInstru;
	}

	public void setSpecialInstru(String specialInstru) {
		this.specialInstru = specialInstru;
	}

	@XmlElement(name = "bed_num")
	public String getBedNum() {
		return bedNum;
	}

	public void setBedNum(String bedNum) {
		this.bedNum = bedNum;
	}

	@XmlElement(name = "hos_depart")
	public String getHosDepart() {
		return hosDepart;
	}

	public void setHosDepart(String hosDepart) {
		this.hosDepart = hosDepart;
	}

	@XmlElement(name = "hospital_num")
	public String getHospitalNum() {
		return hospitalNum;
	}

	public void setHospitalNum(String hospitalNum) {
		this.hospitalNum = hospitalNum;
	}

	@XmlElement(name = "disease_code")
	public String getDiseaseCode() {
		return diseaseCode;
	}

	public void setDiseaseCode(String diseaseCode) {
		this.diseaseCode = diseaseCode;
	}

	@XmlElement(name = "doctor")
	public String getDoctor() {
		return doctor;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	@XmlElement(name = "prescript_remark")
	public String getPrescriptRemark() {
		return prescriptRemark;
	}

	public void setPrescriptRemark(String prescriptRemark) {
		this.prescriptRemark = prescriptRemark;
	}

	@XmlElement(name = "is_hos")
	public String getIsHos() {
		return isHos;
	}

	public void setIsHos(String isHos) {
		this.isHos = isHos;
	}

	@XmlElementWrapper(name = "medici_xq")
	@XmlElement(name = "xq")
	public List<XqReq> getXqReq() {
		return xqReq;
	}

	public void setXqReq(List<XqReq> xqReq) {
		this.xqReq = xqReq;
	}

}
