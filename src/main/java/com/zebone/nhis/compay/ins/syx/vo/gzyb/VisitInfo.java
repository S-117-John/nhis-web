package com.zebone.nhis.compay.ins.syx.vo.gzyb;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;

public class VisitInfo {
	/** PK_VISIT - 主键 */
	@Field(value = "PK_VISIT")
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

	/** PK_VISITCITY - 主键 */
	@Field(value = "PK_VISITCITY")
	private String pkvisitcity;

	/** EU_PVTYPE-就诊类别 */
	@Field(value = "EU_PVTYPE")
	private String euPvType;

	/** CODE_PC-个人电脑号 */
	@Field(value = "CODE_PC")
	private String codePc;

	/** IDNO_GS-工伤就医凭证号 */
	@Field(value = "IDNO_GS")
	private String idnoGs;

	/** IDNO_SY-生育医疗凭证号 */
	@Field(value = "IDNO_SY")
	private String idnoSy;

	/** IDNO_MZMB-门慢申请序列号 */
	@Field(value = "IDNO_MZMB")
	private String idnoMzmb;

	/** TMTYPE_AP-申请待遇类型 */
	@Field(value = "TMTYPE_AP")
	private String tmTypeAp;

	/** TMTYPE-待遇类型 */
	@Field(value = "TMTYPE")
	private String tmType;

	/** CODE_OP-门诊号 */
	@Field(value = "CODE_OP")
	private String codeOp;

	/** CODE_IP-住院号 */
	@Field(value = "CODE_IP")
	private String codeIp;

	/** CODE_UNIT-单位编码 */
	@Field(value = "CODE_UNIT")
	private String codeUnit;

	/** NAME_UNIT-单位名称 */
	@Field(value = "NAME_UNIT")
	private String nameUnit;

	/** CODE_WARD- 病区编码 */
	@Field(value = "CODE_WARD")
	private String codeWard;

	/** NAME_WARD-病区名称 */
	@Field(value = "NAME_WARD")
	private String nameWard;

	/** CODE_DEPT-就诊科室 */
	@Field(value = "CODE_DEPT")
	private String codeDept;

	/** NAME_DEPT-就诊科室名称 */
	@Field(value = "NAME_DEPT")
	private String nameDept;

	/** NAME_DIAG-诊断名称 */
	@Field(value = "NAME_DIAG")
	private String nameDiag;

	/** CODE_DIAG-诊断编码 */
	@Field(value = "CODE_DIAG")
	private String codeDiag;

	/** BALANCE-个人帐户余额 */
	@Field(value = "BALANCE")
	private String balance;

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

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPvcodeIns() {
		return pvcodeIns;
	}

	public void setPvcodeIns(String pvcodeIns) {
		this.pvcodeIns = pvcodeIns;
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

	public String getPkvisitcity() {
		return pkvisitcity;
	}

	public void setPkvisitcity(String pkvisitcity) {
		this.pkvisitcity = pkvisitcity;
	}

	public String getEuPvType() {
		return euPvType;
	}

	public void setEuPvType(String euPvType) {
		this.euPvType = euPvType;
	}

	public String getCodePc() {
		return codePc;
	}

	public void setCodePc(String codePc) {
		this.codePc = codePc;
	}

	public String getIdnoGs() {
		return idnoGs;
	}

	public void setIdnoGs(String idnoGs) {
		this.idnoGs = idnoGs;
	}

	public String getIdnoSy() {
		return idnoSy;
	}

	public void setIdnoSy(String idnoSy) {
		this.idnoSy = idnoSy;
	}

	public String getIdnoMzmb() {
		return idnoMzmb;
	}

	public void setIdnoMzmb(String idnoMzmb) {
		this.idnoMzmb = idnoMzmb;
	}

	public String getTmTypeAp() {
		return tmTypeAp;
	}

	public void setTmTypeAp(String tmTypeAp) {
		this.tmTypeAp = tmTypeAp;
	}

	public String getTmType() {
		return tmType;
	}

	public void setTmType(String tmType) {
		this.tmType = tmType;
	}

	public String getCodeOp() {
		return codeOp;
	}

	public void setCodeOp(String codeOp) {
		this.codeOp = codeOp;
	}

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public String getCodeUnit() {
		return codeUnit;
	}

	public void setCodeUnit(String codeUnit) {
		this.codeUnit = codeUnit;
	}

	public String getNameUnit() {
		return nameUnit;
	}

	public void setNameUnit(String nameUnit) {
		this.nameUnit = nameUnit;
	}

	public String getCodeWard() {
		return codeWard;
	}

	public void setCodeWard(String codeWard) {
		this.codeWard = codeWard;
	}

	public String getNameWard() {
		return nameWard;
	}

	public void setNameWard(String nameWard) {
		this.nameWard = nameWard;
	}

	public String getCodeDept() {
		return codeDept;
	}

	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	public String getNameDiag() {
		return nameDiag;
	}

	public void setNameDiag(String nameDiag) {
		this.nameDiag = nameDiag;
	}

	public String getCodeDiag() {
		return codeDiag;
	}

	public void setCodeDiag(String codeDiag) {
		this.codeDiag = codeDiag;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}
	
	
}
