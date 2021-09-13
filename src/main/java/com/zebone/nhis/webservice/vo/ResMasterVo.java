package com.zebone.nhis.webservice.vo;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;

/**
 * 而患者相关信息 Vo
 * @ClassName: ResMasterVo   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月16日 下午2:35:07     
 * @Copyright: 2019
 */
public class ResMasterVo {
	// 住院号
	private String codeIp;
	// 门诊号
	private String codeOp;
	// 患者姓名
	private String namePi;
	// 患者分类名称
	private String namePicate;
	// 所属机构唯一标识
	private String pkOrg;
	// 患者唯一标识
	private String pkPi;
	// 所属患者分类唯一标识【挂号使用】
	private String pkPicate;
	//新增性别
	private String dtSex;
	//新增生日
	private String birthDate;
	//身份证号
	private String cardNo;
	//身份证号
	private String idNo;
	//民族名称
	private String nameNational;
	//患者编码
	private String codePi;
	//手机号码
	private String mobile;
	
	private BigDecimal amount;

	private String dtcardNo;
	@XmlElement(name = "dtSex")
	public String getDtSex() {
		return dtSex;
	}

	public void setDtSex(String dtSex) {
		this.dtSex = dtSex;
	}
	@XmlElement(name = "dtcardNo")
	public String getDtcardNo() {
		return dtcardNo;
	}

	public void setDtcardNo(String dtcardNo) {
		this.dtcardNo = dtcardNo;
	}

	@XmlElement(name = "birthDate")
	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	@XmlElement(name = "amount")
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@XmlElement(name = "codeIp")
	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	@XmlElement(name = "codeOp")
	public String getCodeOp() {
		return codeOp;
	}

	public void setCodeOp(String codeOp) {
		this.codeOp = codeOp;
	}

	@XmlElement(name = "namePi")
	public String getNamePi() {
		return namePi;
	}
	@XmlElement(name = "idNo")
	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	@XmlElement(name = "namePicate")
	public String getNamePicate() {
		return namePicate;
	}

	public void setNamePicate(String namePicate) {
		this.namePicate = namePicate;
	}

	@XmlElement(name = "pkPi")
	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	@XmlElement(name = "pkPicate")
	public String getPkPicate() {
		return pkPicate;
	}

	public void setPkPicate(String pkPicate) {
		this.pkPicate = pkPicate;
	}

	@XmlElement(name = "pkOrg")
	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	@XmlElement(name = "cardNo")
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	@XmlElement(name = "nameNational")
	public String getNameNational() {
		return nameNational;
	}

	public void setNameNational(String nameNational) {
		this.nameNational = nameNational;
	}

	@XmlElement(name = "codePi")
	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	@XmlElement(name = "mobile")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
