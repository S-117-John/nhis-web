package com.zebone.nhis.pv.pub.vo;

import java.util.Date;

public class PvEncounterListVo {
	/** 工作单位 */
	private String UnitWork;
	/** 医疗卡号 */
	private String Mcno;
	/** 证件类型 */
	private String IdType;
	/** 身份证 */
	private String IdNo;
	/** 患者主键 */
	private String pkPi;
	/** 姓名 */
	private String namePi;

	/** 就诊主键 */
	private String pkPv;

	/** 患者编码 */
	private String codePi;

	/** 住院号 */
	private String codeIp;

	/** 就诊编码 */
	private String codePv;

	/** 床号 */
	private String bedNo;

	/** 科室 */
	private String nameDept;
	/** 科室主键 */
	private String pkDept;

	/** 病区 */
	private String nameDeptNs;
	/** 病区主键 */
	private String pkDeptNs;

	/** 状态 */
	public String euStatus;

	/** 医生 */
	private String nameEmpPhy;
	/** 医生主键 */
	private String pkEmpPhy;

	/** 护士 */
	private String nameEmpNs;
	/** 护士主键 */
	private String pkEmpNs;

	/** 登记人姓名 */
	private String nameEmpReg;

	/** 医保计划 */
	private String nameHp;
	/** 医保计划主键 */
	private String pkInsu;

	/** 入院日期 */
	public Date dateBegin;

	/** 入科日期 */
	public Date dateAdmit;

	/** 出院日期 */
	public Date dateEnd;

	/** 退诊日期 */
	public Date dateCancel;

	/** 退诊标志 */
	public String flagCancel;

	/** 在院标志 */
	public String flagIn;

	/** 结算标志 */
	public String flagSettle;

	/** 性别 */
	private String sex;

	/** 年龄 */
	private String age;

	/** 就诊次数 */
	private String ipTimes;

	/** 出生日期 */
	private Date birthDate;

	/** 患者分类 */
	private String namePicate;

	/** 结算日期 */
	public Date dateSt;

	public String getIpTimes() {
		return ipTimes;
	}

	public void setIpTimes(String ipTimes) {
		this.ipTimes = ipTimes;
	}

	public String getUnitWork() {
		return UnitWork;
	}

	public void setUnitWork(String unitWork) {
		UnitWork = unitWork;
	}

	public String getMcno() {
		return Mcno;
	}

	public void setMcno(String mcno) {
		Mcno = mcno;
	}

	public String getIdType() {
		return IdType;
	}

	public void setIdType(String idType) {
		IdType = idType;
	}

	public String getIdNo() {
		return IdNo;
	}

	public void setIdNo(String idNo) {
		IdNo = idNo;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getNameDeptNs() {
		return nameDeptNs;
	}

	public void setNameDeptNs(String nameDeptNs) {
		this.nameDeptNs = nameDeptNs;
	}

	public String getPkDeptNs() {
		return pkDeptNs;
	}

	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}

	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

	public String getNameEmpPhy() {
		return nameEmpPhy;
	}

	public void setNameEmpPhy(String nameEmpPhy) {
		this.nameEmpPhy = nameEmpPhy;
	}

	public String getPkEmpPhy() {
		return pkEmpPhy;
	}

	public void setPkEmpPhy(String pkEmpPhy) {
		this.pkEmpPhy = pkEmpPhy;
	}

	public String getNameEmpNs() {
		return nameEmpNs;
	}

	public void setNameEmpNs(String nameEmpNs) {
		this.nameEmpNs = nameEmpNs;
	}

	public String getPkEmpNs() {
		return pkEmpNs;
	}

	public void setPkEmpNs(String pkEmpNs) {
		this.pkEmpNs = pkEmpNs;
	}

	public String getNameEmpReg() {
		return nameEmpReg;
	}

	public void setNameEmpReg(String nameEmpReg) {
		this.nameEmpReg = nameEmpReg;
	}

	public String getNameHp() {
		return nameHp;
	}

	public void setNameHp(String nameHp) {
		this.nameHp = nameHp;
	}

	public String getPkInsu() {
		return pkInsu;
	}

	public void setPkInsu(String pkInsu) {
		this.pkInsu = pkInsu;
	}

	public Date getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}

	public Date getDateAdmit() {
		return dateAdmit;
	}

	public void setDateAdmit(Date dateAdmit) {
		this.dateAdmit = dateAdmit;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public Date getDateCancel() {
		return dateCancel;
	}

	public void setDateCancel(Date dateCancel) {
		this.dateCancel = dateCancel;
	}

	public String getFlagCancel() {
		return flagCancel;
	}

	public void setFlagCancel(String flagCancel) {
		this.flagCancel = flagCancel;
	}

	public String getFlagIn() {
		return flagIn;
	}

	public void setFlagIn(String flagIn) {
		this.flagIn = flagIn;
	}

	public String getFlagSettle() {
		return flagSettle;
	}

	public void setFlagSettle(String flagSettle) {
		this.flagSettle = flagSettle;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getNamePicate() {
		return namePicate;
	}

	public void setNamePicate(String namePicate) {
		this.namePicate = namePicate;
	}

	public Date getDateSt() {
		return dateSt;
	}

	public void setDateSt(Date dateSt) {
		this.dateSt = dateSt;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
}
