package com.zebone.nhis.webservice.vo.paidfeevo;

import javax.xml.bind.annotation.XmlElement;

/**
 * 7.3.查询门诊已缴费用
*根据患者查询未时间段内已缴费用信息
 * @ClassName: ResPaidFeeVo   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月20日 上午11:18:33     
 * @Copyright: 2019
 */
public class ResPaidFeeVo {
	private String codeSt;
	// 患者自费金额
	private String amountPi;
	// 结算总金额
	private String amountSt;
	// 患者编码
	private String codePi;
	// 结算日期
	private String dateSt;
	// 结算类型00:门诊收费结算,01:门诊挂号结算,02:诊间支付
	private String dtStype;
	// 结算结果分类0:正常结算，1:欠款结算，2:存款结算，9:未结算
	private String euStresult;
	// 医保名称
	private String hp;
	// 就诊科室名称
	private String nameDept;
	// 结算人员姓名
	private String nameEmpSt;
	// 患者姓名
	private String namePi;
	// 患者就诊主键
	private String pkPv;
	// 结算主键
	private String pkSettle;
	// 取消原因描述
	private String reasonCanc;
	@XmlElement(name = "codeSt")
	public String getCodeSt() {
		return codeSt;
	}

	public void setCodeSt(String codeSt) {
		this.codeSt = codeSt;
	}

	@XmlElement(name = "amountPi")
	public String getAmountPi() {
		return amountPi;
	}

	public void setAmountPi(String amountPi) {
		this.amountPi = amountPi;
	}

	@XmlElement(name = "amountSt")
	public String getAmountSt() {
		return amountSt;
	}

	public void setAmountSt(String amountSt) {
		this.amountSt = amountSt;
	}

	@XmlElement(name = "codePi")
	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	@XmlElement(name = "dateSt")
	public String getDateSt() {
		return dateSt;
	}

	public void setDateSt(String dateSt) {
		this.dateSt = dateSt;
	}

	@XmlElement(name = "dtStype")
	public String getDtStype() {
		return dtStype;
	}

	public void setDtStype(String dtStype) {
		this.dtStype = dtStype;
	}

	@XmlElement(name = "euStresult")
	public String getEuStresult() {
		return euStresult;
	}

	public void setEuStresult(String euStresult) {
		this.euStresult = euStresult;
	}

	@XmlElement(name = "hp")
	public String getHp() {
		return hp;
	}

	public void setHp(String hp) {
		this.hp = hp;
	}

	@XmlElement(name = "nameDept")
	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	@XmlElement(name = "nameEmpSt")
	public String getNameEmpSt() {
		return nameEmpSt;
	}

	public void setNameEmpSt(String nameEmpSt) {
		this.nameEmpSt = nameEmpSt;
	}

	@XmlElement(name = "namePi")
	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	@XmlElement(name = "pkPv")
	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	@XmlElement(name = "pkSettle")
	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	@XmlElement(name = "reasonCanc")
	public String getReasonCanc() {
		return reasonCanc;
	}

	public void setReasonCanc(String reasonCanc) {
		this.reasonCanc = reasonCanc;
	}

}
