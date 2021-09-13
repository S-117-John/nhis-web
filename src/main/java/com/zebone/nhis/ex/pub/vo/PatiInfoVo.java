package com.zebone.nhis.ex.pub.vo;

import java.util.Date;

public class PatiInfoVo {
	private String pkBed; // 床位主键
	private String bedno;// 床号
	private String dtBedtype; // 床位类型

	private String pkPv; // pk_pv
	private String codePv;//就诊编码
	private String codeIp;//住院号
	private String pkPi;// 患者主键
	private String namePi;// 患者姓名
	private String codePi;//患者编码
	private String sex;// 患者的性别
	private String age;// 患者年龄
	private String nameRel;//联系人
	private String telRel;//联系电话
	private String addr;//家庭住址
	
	private String pkDeptNs;//就诊病区
	private String nameDeptNs;//就诊病区
	private String pkDept;//就诊科室
	private String nameDept;//就诊科室名称
	private String nameEmpPhy;// 当前主管医生姓名
	private String nameEmpNs;// 当前责任护士名称
	private Date dateAdmit;// 入科日期
	private Date dateBegin;//入院时间
	private String descDiag;// 诊断名称

	private String dtLevelNs;// 护理等级
	private String dtLevelDise;// 病情等级
	private String dtLevelNutr;// 营养等级
	private String levelNsHint;// 护理等级 - 名称
	private String levelDiseHint;// 病情等级 - 名称
	private String levelNutrHint;// 营养等级 - 名称

	private String hpname; // 主医保
	private String flagInfant;//新生儿标识
	private String flagMaj; // 主床标识
	private String euHold; // 包床主床标识
	private String dtTglevel;//患者分诊等级
    private String name;//床位名称
    private String ipTimes;//就诊次数


	public String getIpTimes() {
		return ipTimes;
	}

	public void setIpTimes(String ipTimes) {
		this.ipTimes = ipTimes;
	}

	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	public String getPkBed() {
		return pkBed;
	}

	public void setPkBed(String pkBed) {
		this.pkBed = pkBed;
	}

	public String getBedno() {
		return bedno;
	}

	public void setBedno(String bedno) {
		this.bedno = bedno;
	}

	public String getDtBedtype() {
		return dtBedtype;
	}

	public void setDtBedtype(String dtBedtype) {
		this.dtBedtype = dtBedtype;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
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

	public String getNameRel() {
		return nameRel;
	}

	public void setNameRel(String nameRel) {
		this.nameRel = nameRel;
	}

	public String getTelRel() {
		return telRel;
	}

	public void setTelRel(String telRel) {
		this.telRel = telRel;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getPkDeptNs() {
		return pkDeptNs;
	}

	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}

	public String getNameDeptNs() {
		return nameDeptNs;
	}

	public void setNameDeptNs(String nameDeptNs) {
		this.nameDeptNs = nameDeptNs;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	public String getNameEmpPhy() {
		return nameEmpPhy;
	}

	public void setNameEmpPhy(String nameEmpPhy) {
		this.nameEmpPhy = nameEmpPhy;
	}

	public String getNameEmpNs() {
		return nameEmpNs;
	}

	public void setNameEmpNs(String nameEmpNs) {
		this.nameEmpNs = nameEmpNs;
	}

	public Date getDateAdmit() {
		return dateAdmit;
	}

	public void setDateAdmit(Date dateAdmit) {
		this.dateAdmit = dateAdmit;
	}

	public Date getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}

	public String getDescDiag() {
		return descDiag;
	}

	public void setDescDiag(String descDiag) {
		this.descDiag = descDiag;
	}

	public String getDtLevelNs() {
		return dtLevelNs;
	}

	public void setDtLevelNs(String dtLevelNs) {
		this.dtLevelNs = dtLevelNs;
	}

	public String getDtLevelDise() {
		return dtLevelDise;
	}

	public void setDtLevelDise(String dtLevelDise) {
		this.dtLevelDise = dtLevelDise;
	}

	public String getDtLevelNutr() {
		return dtLevelNutr;
	}

	public void setDtLevelNutr(String dtLevelNutr) {
		this.dtLevelNutr = dtLevelNutr;
	}

	public String getLevelNsHint() {
		return levelNsHint;
	}

	public void setLevelNsHint(String levelNsHint) {
		this.levelNsHint = levelNsHint;
	}

	public String getLevelDiseHint() {
		return levelDiseHint;
	}

	public void setLevelDiseHint(String levelDiseHint) {
		this.levelDiseHint = levelDiseHint;
	}

	public String getLevelNutrHint() {
		return levelNutrHint;
	}

	public void setLevelNutrHint(String levelNutrHint) {
		this.levelNutrHint = levelNutrHint;
	}

	public String getHpname() {
		return hpname;
	}

	public void setHpname(String hpname) {
		this.hpname = hpname;
	}

	public String getFlagInfant() {
		return flagInfant;
	}

	public void setFlagInfant(String flagInfant) {
		this.flagInfant = flagInfant;
	}

	public String getFlagMaj() {
		return flagMaj;
	}

	public void setFlagMaj(String flagMaj) {
		this.flagMaj = flagMaj;
	}

	public String getEuHold() {
		return euHold;
	}

	public void setEuHold(String euHold) {
		this.euHold = euHold;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDtTglevel() {
		return dtTglevel;
	}

	public void setDtTglevel(String dtTglevel) {
		this.dtTglevel = dtTglevel;
	}

	
}
