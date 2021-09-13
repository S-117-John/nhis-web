package com.zebone.nhis.ma.pub.zsrm.vo;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(value = XmlAccessType.FIELD)
public class PiA1027RegVo {
	//电子健康卡ID
	@XmlElement(name = "ehealth_card_id")
	private String ehealthCardId;
	//健康e卡号
	@XmlElement(name = "HealthCardId ")
	private String healthCardId ;
	//居民健康卡
	//主索引ID
	@XmlElement(name = "mindex_id")
	private String mindexId;
	//证件类型
	@XmlElement(name = "id_type")
	private String idType;

	//证件号
	@XmlElement(name = "id_no")
	private String idNo;
    //用户姓名
	@XmlElement(name = "name")
	private String name;
   //民族
   @XmlElement(name = "nation")
	private  String nation;

	//性别
	@XmlElement(name = "gender")
	private String gender;
	//出生日期
	@XmlElement(name = "birthday")
	private String birthday;
	//手机号码
	@XmlElement(name = "cellphone")
	private  String cellphone;

	//联系电话
	@XmlElement(name = "telephone")
	private String telephone;
	//居住地址
	@XmlElement(name = "address")
	private String address;
	//工作单位
	@XmlElement(name = "unit")
	private  String unit;

	public String getEhealthCardId() {
		return ehealthCardId;
	}

	public void setEhealthCardId(String ehealthCardId) {
		this.ehealthCardId = ehealthCardId;
	}

	public String getHealthCardId() {
		return healthCardId;
	}

	public void setHealthCardId(String healthCardId) {
		this.healthCardId = healthCardId;
	}

	public String getMindexId() {
		return mindexId;
	}

	public void setMindexId(String mindexId) {
		this.mindexId = mindexId;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}
