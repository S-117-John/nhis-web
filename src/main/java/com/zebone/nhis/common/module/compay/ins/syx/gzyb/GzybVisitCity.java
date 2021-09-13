package com.zebone.nhis.common.module.compay.ins.syx.gzyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: INS_GZYB_VISIT_CITY 就诊登记_市医保
 * 
 * @since 2018-07-26 15:35:53
 */
@Table(value = "INS_GZYB_VISIT_CITY")
public class GzybVisitCity extends BaseModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** PK_VISITCITY - 主键 */
	@PK
	@Field(value = "PK_VISITCITY", id = KeyId.UUID)
	private String pkvisitcity;

	/** PK_ORG-所属机构 */
	@Field(value = "PK_ORG")
	private String pkOrg;

	/** PK_VISIT-关联就诊登记 */
	@Field(value = "PK_VISIT")
	private String pkVisit;

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

	/** PERSONTYPE-人员类别 */
	@Field(value = "PERSONTYPE")
	private String persontype;

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

	public String getPkvisitcity() {
		return pkvisitcity;
	}

	public void setPkvisitcity(String pkvisitcity) {
		this.pkvisitcity = pkvisitcity;
	}

	public String getPkVisit() {
		return pkVisit;
	}

	public void setPkVisit(String pkVisit) {
		this.pkVisit = pkVisit;
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

	public String getPersontype() {
		return persontype;
	}

	public void setPersontype(String persontype) {
		this.persontype = persontype;
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

}
