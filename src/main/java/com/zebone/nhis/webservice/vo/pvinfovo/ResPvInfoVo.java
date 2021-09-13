package com.zebone.nhis.webservice.vo.pvinfovo;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

public class ResPvInfoVo {

	private String nameDept="";

	private String nameEmpPhy="";

	private String nameInsu="";

	private String namePicate="";

	private String pkDept="";

	private String pkEmpPhy="";

	private String pkInsu="";

	private String pkPicate="";

	private String dateBegin="";

	private String dateEnd="";

	private String pkPv="";

	private String patientId="";//	病例号

	private String name="";	//患者姓名
	private String sex="";	//患者性别
	private String birthday="";	//出生日期
	private String address="";	//现住址
	private String phone="";	//电话
	private String codeDept=""	;//就诊科室编号
	private String codeDoctor="";	//就诊医生编号
	private String nameDoctor="";	//就诊医生名称
	private String dateClinic="";	//就诊日期
	private String dateStart="";	//发病日期
	private String isReturnVisit="";//	是否复诊	

	@XmlElement(name="patientId")
	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	@XmlElement(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@XmlElement(name="sex")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	@XmlElement(name="birthday")
	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	@XmlElement(name="address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	@XmlElement(name="phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	@XmlElement(name="codeDept")
	public String getCodeDept() {
		return codeDept;
	}

	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}
	@XmlElement(name="codeDoctor")
	public String getCodeDoctor() {
		return codeDoctor;
	}

	public void setCodeDoctor(String codeDoctor) {
		this.codeDoctor = codeDoctor;
	}
	@XmlElement(name="nameDoctor")
	public String getNameDoctor() {
		return nameDoctor;
	}

	public void setNameDoctor(String nameDoctor) {
		this.nameDoctor = nameDoctor;
	}
	@XmlElement(name="dateClinic")
	public String getDateClinic() {
		return dateClinic;
	}

	public void setDateClinic(String dateClinic) {
		this.dateClinic = dateClinic;
	}
	@XmlElement(name="dateStart")
	public String getDateStart() {
		return dateStart;
	}

	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}
	@XmlElement(name="isReturnVisit")
	public String getIsReturnVisit() {
		return isReturnVisit;
	}

	public void setIsReturnVisit(String isReturnVisit) {
		this.isReturnVisit = isReturnVisit;
	}
	
	@XmlElement(name="pkPv")
	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	@XmlElement(name="dateBegin")
	public String getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}

	@XmlElement(name="dateEnd")
	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	@XmlElement(name="nameDept")
	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	@XmlElement(name="nameEmpPhy")
	public String getNameEmpPhy() {
		return nameEmpPhy;
	}

	public void setNameEmpPhy(String nameEmpPhy) {
		this.nameEmpPhy = nameEmpPhy;
	}

	@XmlElement(name="nameInsu")
	public String getNameInsu() {
		return nameInsu;
	}

	public void setNameInsu(String nameInsu) {
		this.nameInsu = nameInsu;
	}

	@XmlElement(name="namePicate")
	public String getNamePicate() {
		return namePicate;
	}

	public void setNamePicate(String namePicate) {
		this.namePicate = namePicate;
	}

	@XmlElement(name="pkDept")
	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	@XmlElement(name="pkEmpPhy")
	public String getPkEmpPhy() {
		return pkEmpPhy;
	}

	public void setPkEmpPhy(String pkEmpPhy) {
		this.pkEmpPhy = pkEmpPhy;
	}

	@XmlElement(name="pkInsu")
	public String getPkInsu() {
		return pkInsu;
	}

	public void setPkInsu(String pkInsu) {
		this.pkInsu = pkInsu;
	}

	@XmlElement(name="pkPicate")
	public String getPkPicate() {
		return pkPicate;
	}

	public void setPkPicate(String pkPicate) {
		this.pkPicate = pkPicate;
	}



}
