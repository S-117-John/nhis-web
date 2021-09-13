package com.zebone.nhis.ma.pub.zsrm.vo;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(value = XmlAccessType.FIELD)
public class PiRegRegisterVo {
	//国籍
	@XmlElement(name = "Nationality")
	private String dtCountry;
	//证件类型
	@XmlElement(name = "DocuType")
	private String dtIdtype;
	//证件号码
	@XmlElement(name = "DocuId")
	private String idNo;
	//申请方式
	@XmlElement(name = "applyType")
	private String applyType;
	//实体卡卡号
	@XmlElement(name = "IcCardId")
	private String icCardId;
	//实体卡类型
	@XmlElement(name = "Type")
	private String type;
	//实体卡类型
	@XmlElement(name = "Name")
	private String namePi;
	//出生日期
	@XmlElement(name = "Birth")
	private String birth;
	//性别
	@XmlElement(name = "Sex")
	private String sex;
	//民族
	@XmlElement(name = "Nation")
	private String nation;
	//地址（户口所在地）
	@XmlElement(name = "Address2")
	private String address2;
	//本人手机
	@XmlElement(name = "Phone")
	private String phone;
	//申请时间
	@XmlElement(name = "AppliDate")
	private String appliDate;




	public String getDtCountry() {
		return dtCountry;
	}

	public void setDtCountry(String dtCountry) {
		this.dtCountry = dtCountry;
	}

	public String getDtIdtype() {
		return dtIdtype;
	}

	public void setDtIdtype(String dtIdtype) {
		this.dtIdtype = dtIdtype;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public String getIcCardId() {
		return icCardId;
	}

	public void setIcCardId(String icCardId) {
		this.icCardId = icCardId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAppliDate() {
		return appliDate;
	}

	public void setAppliDate(String appliDate) {
		this.appliDate = appliDate;
	}
}
