package com.zebone.nhis.compay.ins.shenzhen.vo.szxnh;

import java.math.BigDecimal;

import com.zebone.platform.modules.dao.build.au.Field;

/**
 * 广州新农合医保结算单打印表头信息
 * @author Administrator
 *
 */
public class SettlePageHeaderVo {
	@Field(value = "NamePi")
	private String namePi;//患者姓名
	@Field(value = "Idno")
	private String idno;//身份证号

	private String age;//年龄
	@Field(value = "Birthday")
	private String birthday;//出生年月日
	@Field(value = "OrgName")
	private String orgName;//医疗机构名称 
	@Field(value = "CodeIp")
	private String codeIp;//住院号 
	@Field(value = "NameMainDiag")
	private String nameMainDiag;//主要诊断
	@Field(value = "DateInt")
	private String dateInt;//入院日期
	@Field(value = "DateOut")
	private String dateOut;//出院日期
	
	private String days;//住院天数
	@Field(value = "DateCC")
	private String dateCC;//结账日期
	@Field(value = "Amount")
	private BigDecimal amount;//总费用
	@Field(value = "AmtKbx")
	private BigDecimal amtKbx;//基本农合可补费用
	@Field(value = "AmtSjbx")
	private BigDecimal amtSjbx;//基本农合补偿费用 
	@Field(value = "AmtBngljbx")
	private BigDecimal amtBngljbx;//基本农合累计补偿
	@Field(value = "AmtDbbxkbc")
	private BigDecimal amtDbbxkbc;//大病可补费用
	@Field(value = "AmtDbbxsjbc")
	private BigDecimal amtDbbxsjbc;//大病补偿费用 
	@Field(value = "AmtDf")
	private BigDecimal amtDf;//直报金额小写 
	
	private String amtDfCapital;//直报金额大写
	@Field(value = "AmtQfx")
	private BigDecimal amtQfx;//基本农合起付线
	@Field(value = "AmtGrzf")
	private BigDecimal amtGrzf;//自付金额小写
	
	private String amtGrzfCapital;//自付金额大写 
	@Field(value = "AmtFdx")
	private BigDecimal amtFdx;//基本农合封顶线 
	
	private String patientAddress;//患者地址
	@Field(value = "NameChs")
	private String nameChs;//参合省名称
	@Field(value = "NameChsh")
	private String nameChsh;//参合市名称
	@Field(value = "NameChqx")
	private String nameChqx;//参合区县名称
	@Field(value = "PaticontNumber")
	private String paticontNumber;//联系电话

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public String getNameMainDiag() {
		return nameMainDiag;
	}

	public void setNameMainDiag(String nameMainDiag) {
		this.nameMainDiag = nameMainDiag;
	}

	public String getDateInt() {
		return dateInt;
	}

	public void setDateInt(String dateInt) {
		this.dateInt = dateInt;
	}

	public String getDateOut() {
		return dateOut;
	}

	public void setDateOut(String dateOut) {
		this.dateOut = dateOut;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getDateCC() {
		return dateCC;
	}

	public void setDateCC(String dateCC) {
		this.dateCC = dateCC;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmtKbx() {
		return amtKbx;
	}

	public void setAmtKbx(BigDecimal amtKbx) {
		this.amtKbx = amtKbx;
	}

	public BigDecimal getAmtSjbx() {
		return amtSjbx;
	}

	public void setAmtSjbx(BigDecimal amtSjbx) {
		this.amtSjbx = amtSjbx;
	}

	public BigDecimal getAmtBngljbx() {
		return amtBngljbx;
	}

	public void setAmtBngljbx(BigDecimal amtBngljbx) {
		this.amtBngljbx = amtBngljbx;
	}

	public BigDecimal getAmtDbbxkbc() {
		return amtDbbxkbc;
	}

	public void setAmtDbbxkbc(BigDecimal amtDbbxkbc) {
		this.amtDbbxkbc = amtDbbxkbc;
	}

	public BigDecimal getAmtDbbxsjbc() {
		return amtDbbxsjbc;
	}

	public void setAmtDbbxsjbc(BigDecimal amtDbbxsjbc) {
		this.amtDbbxsjbc = amtDbbxsjbc;
	}

	public BigDecimal getAmtDf() {
		return amtDf;
	}

	public void setAmtDf(BigDecimal amtDf) {
		this.amtDf = amtDf;
	}

	public String getAmtDfCapital() {
		return amtDfCapital;
	}

	public void setAmtDfCapital(String amtDfCapital) {
		this.amtDfCapital = amtDfCapital;
	}

	public BigDecimal getAmtQfx() {
		return amtQfx;
	}

	public void setAmtQfx(BigDecimal amtQfx) {
		this.amtQfx = amtQfx;
	}

	public BigDecimal getAmtGrzf() {
		return amtGrzf;
	}

	public void setAmtGrzf(BigDecimal amtGrzf) {
		this.amtGrzf = amtGrzf;
	}

	public String getAmtGrzfCapital() {
		return amtGrzfCapital;
	}

	public void setAmtGrzfCapital(String amtGrzfCapital) {
		this.amtGrzfCapital = amtGrzfCapital;
	}

	public BigDecimal getAmtFdx() {
		return amtFdx;
	}

	public void setAmtFdx(BigDecimal amtFdx) {
		this.amtFdx = amtFdx;
	}

	public String getPatientAddress() {
		return patientAddress;
	}

	public void setPatientAddress(String patientAddress) {
		this.patientAddress = patientAddress;
	}

	public String getNameChs() {
		return nameChs;
	}

	public void setNameChs(String nameChs) {
		this.nameChs = nameChs;
	}

	public String getNameChsh() {
		return nameChsh;
	}

	public void setNameChsh(String nameChsh) {
		this.nameChsh = nameChsh;
	}

	public String getNameChqx() {
		return nameChqx;
	}

	public void setNameChqx(String nameChqx) {
		this.nameChqx = nameChqx;
	}

	public String getPaticontNumber() {
		return paticontNumber;
	}

	public void setPaticontNumber(String paticontNumber) {
		this.paticontNumber = paticontNumber;
	}
	
	
}
