package com.zebone.nhis.webservice.vo.hospinfobedvo;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;

public class ResHospInfoBedVo {
	// 科室
	private String pkDept = "";
	// 住院号[可选]
	private String codeIp = "";
	// 患者唯一标识
	private String pkPi = "";
	// 床号
	private String bedNo = "";
	// 就诊开始日期
	private String dateBegin = "";
	// 就诊结束日期
	private String dateEnd = "";
	// 当前就诊科室编码
	private String codeDept = "";
	// 当前就诊科室名称
	private String nameDept = "";
	
	// 当前所在病区编码
	private String codeDeptNs = "";
	// 当前所在病区名称
	private String nameDeptNs = "";
	// 责任护士名称
	private String nameEmpNs = "";
	// 主治医生名称
	private String nameEmpPhy = "";
	// 主医保计划名称
	private String nameInsu = "";
	// 患者分类名称
	private String namePicate = "";
	// 当前所在病区唯一标识
	private String pkDeptNs = "";
	// 责任护士唯一标识
	private String pkEmpNs = "";
	// 主治医生唯一标识
	private String pkEmpPhy = "";
	// 主医保计划唯一标识
	private String pkInsu = "";
	// 患者分类唯一标识
	private String pkPicate = "";
	//患者姓名 
	private String name = "";
	
	private String dateAdmit = "";
	
	private String birthday = "";
	
	private String age = "";
	
	private String sex = "";
	
	private String nation = "";
	
	private String idCard = "";
	
	private String phoneNum = "";
	
	private String contactName = "";
	
	private String contactRelationship = "";
	
	private String contactPhoneNum = "";
	
	private String roomNum = "";
	
	private String bedType = "";
	
	private String isLeaveHospital = "";
	
	private String responsibleDoctorNum = "";
	
	private String responsibleDoctor = "";
	
	private String responsibleNurseNum = "";
	
	private String responsibleNurse = "";
	
	private String expenseType = "";
	
	private String processedCardNum = "";
	
	private String cardtype = "";
	
	private String doctorAdvicelCode = "";
	
	private String doctorAdvice = "";
	
	private String antihistamines = "";
	
	private String pkPv = "";
	
	private String codePv = "";
	
	
	
	
	//预缴金
	private BigDecimal prePayment = new BigDecimal(0.0);
	//患者余额
	private BigDecimal balance = new BigDecimal(0.0);
	
	//实际消费金额
	private BigDecimal patientInvest = new BigDecimal(0.0);
	//患者担保额
	private BigDecimal creditMoney = new BigDecimal(0.0);
	
	@XmlElement(name = "codePv")
	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "birthday")
	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
    
	@XmlElement(name = "age")
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
    
	@XmlElement(name = "sex")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	@XmlElement(name = "nation")
	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}
	
	@XmlElement(name = "idCard")
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
	@XmlElement(name = "phoneNum")
	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	
	@XmlElement(name = "contactName")
	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
    
	@XmlElement(name = "contactRelationship")
	public String getContactRelationship() {
		return contactRelationship;
	}

	public void setContactRelationship(String contactRelationship) {
		this.contactRelationship = contactRelationship;
	}
    
	@XmlElement(name = "contactPhoneNum")
	public String getContactPhoneNum() {
		return contactPhoneNum;
	}

	public void setContactPhoneNum(String contactPhoneNum) {
		this.contactPhoneNum = contactPhoneNum;
	}
	
	@XmlElement(name = "roomNum")
	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}
    
	@XmlElement(name = "bedType")
	public String getBedType() {
		return bedType;
	}

	public void setBedType(String bedType) {
		this.bedType = bedType;
	}
    
	@XmlElement(name = "isLeaveHospital")
	public String getIsLeaveHospital() {
		return isLeaveHospital;
	}

	public void setIsLeaveHospital(String isLeaveHospital) {
		this.isLeaveHospital = isLeaveHospital;
	}
    
	@XmlElement(name = "responsibleDoctorNum")
	public String getResponsibleDoctorNum() {
		return responsibleDoctorNum;
	}

	public void setResponsibleDoctorNum(String responsibleDoctorNum) {
		this.responsibleDoctorNum = responsibleDoctorNum;
	}
	
	@XmlElement(name = "responsibleDoctor")
	public String getResponsibleDoctor() {
		return responsibleDoctor;
	}

	public void setResponsibleDoctor(String responsibleDoctor) {
		this.responsibleDoctor = responsibleDoctor;
	}
    
	@XmlElement(name = "responsibleNurseNum")
	public String getResponsibleNurseNum() {
		return responsibleNurseNum;
	}

	public void setResponsibleNurseNum(String responsibleNurseNum) {
		this.responsibleNurseNum = responsibleNurseNum;
	}
	
	@XmlElement(name = "responsibleNurse")
	public String getResponsibleNurse() {
		return responsibleNurse;
	}

	public void setResponsibleNurse(String responsibleNurse) {
		this.responsibleNurse = responsibleNurse;
	}
    
	@XmlElement(name = "expenseType")
	public String getExpenseType() {
		return expenseType;
	}

	public void setExpenseType(String expenseType) {
		this.expenseType = expenseType;
	}
    
	@XmlElement(name = "processedCardNum")
	public String getProcessedCardNum() {
		return processedCardNum;
	}

	public void setProcessedCardNum(String processedCardNum) {
		this.processedCardNum = processedCardNum;
	}
    
	@XmlElement(name = "cardtype")
	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}
    
	@XmlElement(name = "doctorAdvicelCode")
	public String getDoctorAdvicelCode() {
		return doctorAdvicelCode;
	}

	public void setDoctorAdvicelCode(String doctorAdvicelCode) {
		this.doctorAdvicelCode = doctorAdvicelCode;
	}
	
	@XmlElement(name = "doctorAdvice")
	public String getDoctorAdvice() {
		return doctorAdvice;
	}

	public void setDoctorAdvice(String doctorAdvice) {
		this.doctorAdvice = doctorAdvice;
	}
	
	@XmlElement(name = "antihistamines")
	public String getAntihistamines() {
		return antihistamines;
	}

	public void setAntihistamines(String antihistamines) {
		this.antihistamines = antihistamines;
	}

	@XmlElement(name = "pkPv")
	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}


	
	@XmlElement(name = "prePayment")
	public BigDecimal getPrePayment() {
		return prePayment;
	}

	public void setPrePayment(BigDecimal prePayment) {
		this.prePayment = prePayment;
	}

	@XmlElement(name = "balance")
	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	@XmlElement(name = "patientInvest")
	public BigDecimal getPatientInvest() {
		return patientInvest;
	}

	public void setPatientInvest(BigDecimal patientInvest) {
		this.patientInvest = patientInvest;
	}

	@XmlElement(name = "creditMoney")
	public BigDecimal getCreditMoney() {
		return creditMoney;
	}

	public void setCreditMoney(BigDecimal creditMoney) {
		this.creditMoney = creditMoney;
	}


	@XmlElement(name = "bedNo")
	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	@XmlElement(name = "dateBegin")
	public String getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}

	@XmlElement(name = "dateEnd")
	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	@XmlElement(name = "nameDept")
	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	@XmlElement(name = "nameDeptNs")
	public String getNameDeptNs() {
		return nameDeptNs;
	}

	public void setNameDeptNs(String nameDeptNs) {
		this.nameDeptNs = nameDeptNs;
	}

	@XmlElement(name = "nameEmpNs")
	public String getNameEmpNs() {
		return nameEmpNs;
	}

	public void setNameEmpNs(String nameEmpNs) {
		this.nameEmpNs = nameEmpNs;
	}

	@XmlElement(name = "nameEmpPhy")
	public String getNameEmpPhy() {
		return nameEmpPhy;
	}

	public void setNameEmpPhy(String nameEmpPhy) {
		this.nameEmpPhy = nameEmpPhy;
	}

	@XmlElement(name = "nameInsu")
	public String getNameInsu() {
		return nameInsu;
	}

	public void setNameInsu(String nameInsu) {
		this.nameInsu = nameInsu;
	}

	@XmlElement(name = "namePicate")
	public String getNamePicate() {
		return namePicate;
	}

	public void setNamePicate(String namePicate) {
		this.namePicate = namePicate;
	}

	@XmlElement(name = "pkDeptNs")
	public String getPkDeptNs() {
		return pkDeptNs;
	}

	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}

	@XmlElement(name = "pkEmpNs")
	public String getPkEmpNs() {
		return pkEmpNs;
	}

	public void setPkEmpNs(String pkEmpNs) {
		this.pkEmpNs = pkEmpNs;
	}

	@XmlElement(name = "pkEmpPhy")
	public String getPkEmpPhy() {
		return pkEmpPhy;
	}

	public void setPkEmpPhy(String pkEmpPhy) {
		this.pkEmpPhy = pkEmpPhy;
	}

	@XmlElement(name = "pkInsu")
	public String getPkInsu() {
		return pkInsu;
	}

	public void setPkInsu(String pkInsu) {
		this.pkInsu = pkInsu;
	}

	@XmlElement(name = "pkPicate")
	public String getPkPicate() {
		return pkPicate;
	}

	public void setPkPicate(String pkPicate) {
		this.pkPicate = pkPicate;
	}

	@XmlElement(name = "codeIp")
	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	@XmlElement(name = "pkPi")
	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	@XmlElement(name = "pkDept")
	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getCodeDept() {
		return codeDept;
	}

	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}

	public String getCodeDeptNs() {
		return codeDeptNs;
	}

	public void setCodeDeptNs(String codeDeptNs) {
		this.codeDeptNs = codeDeptNs;
	}

	public String getDateAdmit() {
		return dateAdmit;
	}

	public void setDateAdmit(String dateAdmit) {
		this.dateAdmit = dateAdmit;
	}

}
