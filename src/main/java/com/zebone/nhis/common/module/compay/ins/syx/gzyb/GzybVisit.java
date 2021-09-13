package com.zebone.nhis.common.module.compay.ins.syx.gzyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: INS_GZYB_VISIT 就诊登记
 * 
 * @since 2018-07-26 14:35:53
 */
@Table(value = "INS_GZYB_VISIT")
public class GzybVisit extends BaseModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** PK_VISIT - 主键 */
	@PK
	@Field(value = "PK_VISIT", id = KeyId.UUID)
	private String pkvisit;

	/** PK_HP - PK_HP-医保计划主键 */
	@Field(value = "PK_HP")
	private String pkhp;

	/** PK_ORG-所属机构 */
	@Field(value = "PK_ORG")
	private String pkOrg;

	/** PVCODE_INS-就医登记号 */
	@Field(value = "PVCODE_INS")
	private String pvcodeIns;
	
	public String getPvcodeIns() {
		return pvcodeIns;
	}

	public void setPvcodeIns(String pvcodeIns) {
		this.pvcodeIns = pvcodeIns;
	}

	/** PK_PV-就诊主键 */
	@Field(value = "PK_PV")
	private String pkpv;

	/** PK_PI- 患者主键 */
	@Field(value = "PK_PI")
	private String pkpi;

	/** CODE_ORG-医院编号 */
	@Field(value = "CODE_ORG")
	private String codeOrg;

	/** CODE_CENTER-中心编号 */
	@Field(value = "CODE_CENTER")
	private String codeCenter;

	/** NAME_ORG-医院名称-定点医疗机构名称 */
	@Field(value = "NAME_ORG")
	private String nameOrg;

	/** NAME_PI-患者姓名 */
	@Field(value = "NAME_PI")
	private String namePi;

	/** DT_SEX-性别代码 */
	@Field(value = "DT_SEX")
	private String dtSex;

	/** NAME_SEX-性别名称 */
	@Field(value = "NAME_SEX")
	private String nameSex;

	/** PERSONTYPE-人员类别 */
	@Field(value = "PERSONTYPE")
	private String persontype;

	/** BIRTH_DATE-出生日期 */
	@Field(value = "BIRTH_DATE")
	private Date birthDate;

	/** IDNO-公民身份号码 */
	@Field(value = "IDNO")
	private String idno;

	/** DATE_REG-登记日期 */
	@Field(value = "DATE_REG")
	private Date dateReg;

	/** EU_STATUS_ST-结算状态 */
	@Field(value = "EU_STATUS_ST")
	private String euStatusSt;

	/** NOTE-备注 */
	@Field(value = "NOTE")
	private String note;

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

	public String getPkvisit() {
		return pkvisit;
	}

	public void setPkvisit(String pkvisit) {
		this.pkvisit = pkvisit;
	}

	public String getPkhp() {
		return pkhp;
	}

	public void setPkhp(String pkhp) {
		this.pkhp = pkhp;
	}

	public String getPkpv() {
		return pkpv;
	}

	public void setPkpv(String pkpv) {
		this.pkpv = pkpv;
	}

	public String getPkpi() {
		return pkpi;
	}

	public void setPkpi(String pkpi) {
		this.pkpi = pkpi;
	}

	public String getCodeOrg() {
		return codeOrg;
	}

	public void setCodeOrg(String codeOrg) {
		this.codeOrg = codeOrg;
	}

	public String getCodeCenter() {
		return codeCenter;
	}

	public void setCodeCenter(String codeCenter) {
		this.codeCenter = codeCenter;
	}

	public String getNameOrg() {
		return nameOrg;
	}

	public void setNameOrg(String nameOrg) {
		this.nameOrg = nameOrg;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getDtSex() {
		return dtSex;
	}

	public void setDtSex(String dtSex) {
		this.dtSex = dtSex;
	}

	public String getNameSex() {
		return nameSex;
	}

	public void setNameSex(String nameSex) {
		this.nameSex = nameSex;
	}

	public String getPersontype() {
		return persontype;
	}

	public void setPersontype(String persontype) {
		this.persontype = persontype;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public Date getDateReg() {
		return dateReg;
	}

	public void setDateReg(Date dateReg) {
		this.dateReg = dateReg;
	}

	public String getEuStatusSt() {
		return euStatusSt;
	}

	public void setEuStatusSt(String euStatusSt) {
		this.euStatusSt = euStatusSt;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
