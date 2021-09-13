package com.zebone.nhis.ma.pub.zsba.vo;

import java.util.Date;

public class OrderExVo {
	private String nameOrd;//药品名称
	private String spec;//规格
	private Integer dosage;//剂量
	private String unitDos;//剂量单位
	private Integer quan;//用量
	private String unitPack;//用量单位
    private String pkPv;//就诊主键
    private Date datePlan; 
	private String nameDept;//就诊科室
	private String nameDeptNs;//就诊病区
	private String bedNo;
	private String codeIp;
	private String namePi;
	private String sex;
    private String pkExocc;
    private String datePlanDescr;//执行时间显示用YYYY-MM-DD HH:MI
    private String pkPi;
    private String pkPdDe;//药袋执行唯一标识
    private String codeDe;//发药单号
    private String euAlways;//长期临时标志
    private String pkCnord;//医嘱主键
    private String prtId;//药袋唯一标识（打印时按此字段分组）
    private String nameSupply;//用法
	private String nameFreq;//频次说明
	private  String codeBag; //药袋编码
	private String pkMedbag;//药袋主键
	private String  euBag; //药袋类型 01-摆药机 02-人工药袋
	private  String   ordsn ;//医嘱序号
	/**
	 *请领明细主键
	 */
	private String pkPdapdt;

	/**
	 *药品发放主键
	 */
	private String  pkPdde;

	/**
	 *是否走包药机
	 */
	private String  valAtt;



	public String getOrdsn() {
		return ordsn;
	}

	public void setOrdsn(String ordsn) {
		this.ordsn = ordsn;
	}

	public String getPkMedbag() {
		return pkMedbag;
	}

	public void setPkMedbag(String pkMedbag) {
		this.pkMedbag = pkMedbag;
	}

	public String getEuBag() {
		return euBag;
	}

	public void setEuBag(String euBag) {
		this.euBag = euBag;
	}

	public String getCodeBag() {
		return codeBag;
	}

	public void setCodeBag(String codeBag) {
		this.codeBag = codeBag;
	}



	public String getPkPdapdt() {
		return pkPdapdt;
	}

	public void setPkPdapdt(String pkPdapdt) {
		this.pkPdapdt = pkPdapdt;
	}

	public String getPkPdde() {
		return pkPdde;
	}

	public void setPkPdde(String pkPdde) {
		this.pkPdde = pkPdde;
	}

	public String getValAtt() {
		return valAtt;
	}

	public void setValAtt(String valAtt) {
		this.valAtt = valAtt;
	}

	public String getPkCgip() {
		return pkCgip;
	}

	public void setPkCgip(String pkCgip) {
		this.pkCgip = pkCgip;
	}

	/**
	 *计费主键
	 */
	private String  pkCgip;


	public String getNameSupply() {
		return nameSupply;
	}

	public void setNameSupply(String nameSupply) {
		this.nameSupply = nameSupply;
	}

	public String getNameFreq() {
		return nameFreq;
	}

	public void setNameFreq(String nameFreq) {
		this.nameFreq = nameFreq;
	}

	public String getPrtId() {
		return prtId;
	}

	public void setPrtId(String prtId) {
		this.prtId = prtId;
	}

	public String getEuAlways() {
		return euAlways;
	}

	public void setEuAlways(String euAlways) {
		this.euAlways = euAlways;
	}

	public String getPkCnord() {
		return pkCnord;
	}

	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}

	public String getCodeDe() {
		return codeDe;
	}

	public void setCodeDe(String codeDe) {
		this.codeDe = codeDe;
	}

	public String getNameDeptNs() {
		return nameDeptNs;
	}

	public void setNameDeptNs(String nameDeptNs) {
		this.nameDeptNs = nameDeptNs;
	}

	public String getPkPdDe() {
		return pkPdDe;
	}

	public void setPkPdDe(String pkPdDe) {
		this.pkPdDe = pkPdDe;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public String getDatePlanDescr() {
		return datePlanDescr;
	}

	public void setDatePlanDescr(String datePlanDescr) {
		this.datePlanDescr = datePlanDescr;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPkExocc() {
		return pkExocc;
	}

	public void setPkExocc(String pkExocc) {
		this.pkExocc = pkExocc;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public Date getDatePlan() {
		return datePlan;
	}

	public void setDatePlan(Date datePlan) {
		this.datePlan = datePlan;
	}

	public String getNameOrd() {
		return nameOrd;
	}

	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public Integer getDosage() {
		return dosage;
	}

	public void setDosage(Integer dosage) {
		this.dosage = dosage;
	}

	public String getUnitDos() {
		return unitDos;
	}

	public void setUnitDos(String unitDos) {
		this.unitDos = unitDos;
	}
	public Integer getQuan() {
		return quan;
	}
	public void setQuan(Integer quan) {
		this.quan = quan;
	}
	public String getUnitPack() {
		return unitPack;
	}
	public void setUnitPack(String unitPack) {
		this.unitPack = unitPack;
	}

}
