package com.zebone.nhis.common.module.compay.ins.shenzhen.xnh;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: INS_SZYB_ST 医保结算
 * 
 * @since 2019-12-12 14:35:53
 */
@Table(value = "INS_SZYB_ST")
public class SzybSt extends BaseModule {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** PK_INSST - 主键 */
	@PK
	@Field(value = "PK_INSST", id = KeyId.UUID)
	private String pkInsst;

	@Field(value = "PK_VISIT")
	private String pkvisit;
	
	/** PK_HP - 医保类别 */
	@Field(value = "PK_HP")
	private String pkHp;

	/** PK_PV - 病人就诊主键 */
	@Field(value = "PK_PV")
	private String pkPv;

	/** PK_PI - 患者主键 */
	@Field(value = "PK_PI")
	private String pkPi;

	/** DATE_INP - 入院日期 */
	@Field(value = "DATE_INP")
	private Date dateInp;

	/** DATE_OUTP - 出院日期 */
	@Field(value = "DATE_OUTP")
	private Date dateOutp;

	/** DAYS - 实际住院天数 */
	@Field(value = "DAYS")
	private Integer days;

	/** PK_SETTLE - 结算主键 */
	@Field(value = "PK_SETTLE")
	private String pkSettle;

	/** PVCODE_INS - 就医登记号 */
	@Field(value = "PVCODE_INS")
	private String pvcodeIns;

	/** DATE_ST - 结算日期 */
	@Field(value = "DATE_ST")
	private Date dateSt;

	/** AMOUNT - 总金额 */
	@Field(value = "AMOUNT")
	private Double amount;

	/** CODE_CENTER - 中心编码 */
	@Field(value = "CODE_CENTER")
	private String codeCenter;

	/** CODE_ORG - 医院编号 */
	@Field(value = "CODE_ORG")
	private String codeOrg;

	/** NAME_ORG - 医院名称 */
	@Field(value = "NAME_ORG")
	private String nameOrg;

	/** PK_ORG-所属机构 */
	@Field(value = "PK_ORG")
	private String pkOrg;
	
	/** CREATOR-创建人 */
	@Field(value = "CREATOR")
	private String creator;

	/** CREATE_TIME-创建时间 */
	@Field(value = "CREATE_TIME")
	private Date createTime;

	/** DEL_FLAG-删除标志 */
	@Field(value = "DEL_FLAG")
	private String delFlag;

	/** TS-时间戳 */
	@Field(value = "TS")
	private String ts;
	
	
	public String getPkInsst() {
		return this.pkInsst;
	}

	public void setPkInsst(String pkInsst) {
		this.pkInsst = pkInsst;
	}

	public String getPkHp() {
		return this.pkHp;
	}

	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}

	public String getPkPv() {
		return this.pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getPkPi() {
		return this.pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public Date getDateInp() {
		return this.dateInp;
	}

	public void setDateInp(Date dateInp) {
		this.dateInp = dateInp;
	}

	public Date getDateOutp() {
		return this.dateOutp;
	}

	public void setDateOutp(Date dateOutp) {
		this.dateOutp = dateOutp;
	}

	public Integer getDays() {
		return this.days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public String getPkSettle() {
		return this.pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	public String getPvcodeIns() {
		return this.pvcodeIns;
	}

	public void setPvcodeIns(String pvcodeIns) {
		this.pvcodeIns = pvcodeIns;
	}

	public Date getDateSt() {
		return this.dateSt;
	}

	public void setDateSt(Date dateSt) {
		this.dateSt = dateSt;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getCodeCenter() {
		return this.codeCenter;
	}

	public void setCodeCenter(String codeCenter) {
		this.codeCenter = codeCenter;
	}

	public String getCodeOrg() {
		return this.codeOrg;
	}

	public void setCodeOrg(String codeOrg) {
		this.codeOrg = codeOrg;
	}

	public String getNameOrg() {
		return this.nameOrg;
	}

	public void setNameOrg(String nameOrg) {
		this.nameOrg = nameOrg;
	}

	public String getPkvisit() {
		return pkvisit;
	}

	public void setPkvisit(String pkvisit) {
		this.pkvisit = pkvisit;
	}
	
}
