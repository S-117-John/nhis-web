package com.zebone.nhis.webservice.sd.vo;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value = "PV_DIAG")
public class PvDiag {
	@Field(value = "PK_DCDT")
	private String pkDcdt;

	@Field(value = "CODE_DCDT")
	private String codeDcdt;

	@Field(value = "PK_CCDT")
	private String pkCcdt;

	@Field(value = "CODE_CCDT")
	private String codeCcdt;

	@Field(value = "DESC_BODYPART")
	private String descBodypart;

	@Field(value = "DESC_DRGPROP")
	private String descDrgprop;

	/** PK_PVDIAG - 就诊诊断主键 */
	@PK
	@Field(value = "PK_PVDIAG", id = KeyId.UUID)
	private String pkPvdiag;
	
	@Field(value = "PK_ORG")
	private String pkOrg;

	/** PK_PV - 就诊主键 */
	@Field(value = "PK_PV")
	private String pkPv;

	/** SORT_NO - 诊断序号 */
	@Field(value = "SORT_NO")
	private Long sortNo;

	/**
	 * DT_DIAGTYPE - 诊断类型 0X 门诊过程：00 门诊诊断。 1X 住院过程：10 入院诊断，11 手术诊断，12 出院诊断。 2X
	 * 医技辅助：20 放射诊断，21 病理诊断
	 */
	@Field(value = "DT_DIAGTYPE")
	private String dtDiagtype;

	/** PK_DIAG - 诊断编码 */
	@Field(value = "PK_DIAG")
	private String pkDiag;

	/** DESC_DIAG - 诊断描述 */
	@Field(value = "DESC_DIAG")
	private String descDiag;

	/** FLAG_MAJ - 主诊断标志 */
	@Field(value = "FLAG_MAJ")
	private String flagMaj;

	/** FLAG_SUSP - 疑似诊断标志 */
	@Field(value = "FLAG_SUSP")
	private String flagSusp;

	/** FLAG_CONTAGION - 传染病标志 */
	@Field(value = "FLAG_CONTAGION")
	private String flagContagion;

	/** DATE_DIAG - 诊断日期 */
	@Field(value = "DATE_DIAG")
	private Date dateDiag;

	/** PK_EMP_DIAG - 诊断医生 */
	@Field(value = "PK_EMP_DIAG")
	private String pkEmpDiag;

	/** NAME_EMP_DIAG - 诊断医生姓名 */
	@Field(value = "NAME_EMP_DIAG")
	private String nameEmpDiag;

	/** FLAG_FINALLY - 确诊标志 */
	@Field(value = "FLAG_FINALLY")
	private String flagFinally;

	/** FLAG_CURE - 治愈标志 */
	@Field(value = "FLAG_CURE")
	private String flagCure;

	/** MODIFIER - 修改人 */
	@Field(value = "MODIFIER")
	private String modifier;

	@Field(value = "DT_TREATWAY")
	private String dtTreatway;

	@Field(value = "VAL")
	private Double val;

	@Field(value = "PK_FATHER")
	private String pkFather;


	@Field(value = "NAME_DIAG")
	private String nameDiag;

	@Field(value = "CODE_ICD")
	private String codeIcd;

	@Field(value = "EU_ADMCON")
	private String euAdmcon;

	@Field(value = "EU_SPTYPE")
	private String euSptype;

	private String nameDcdt;

	public String getPkPvdiag() {
		return this.pkPvdiag;
	}
	public void setPkPvdiag(String pkPvdiag) {
		this.pkPvdiag = pkPvdiag;
	}

	public String getPkPv() {
		return this.pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public Long getSortNo() {
		return this.sortNo;
	}
	public void setSortNo(Long sortNo) {
		this.sortNo = sortNo;
	}

	public String getNameDcdt() {
		return nameDcdt;
	}
	public void setNameDcdt(String nameDcdt) {
		this.nameDcdt = nameDcdt;
	}
	public String getDtDiagtype() {
		return this.dtDiagtype;
	}
	public void setDtDiagtype(String dtDiagtype) {
		this.dtDiagtype = dtDiagtype;
	}

	public String getPkDiag() {
		return this.pkDiag;
	}
	public void setPkDiag(String pkDiag) {
		this.pkDiag = pkDiag;
	}

	public String getDescDiag() {
		return this.descDiag;
	}
	public void setDescDiag(String descDiag) {
		this.descDiag = descDiag;
	}

	public String getFlagMaj() {
		return this.flagMaj;
	}
	public void setFlagMaj(String flagMaj) {
		this.flagMaj = flagMaj;
	}

	public String getFlagSusp() {
		return this.flagSusp;
	}
	public void setFlagSusp(String flagSusp) {
		this.flagSusp = flagSusp;
	}

	public String getFlagContagion() {
		return this.flagContagion;
	}
	public void setFlagContagion(String flagContagion) {
		this.flagContagion = flagContagion;
	}

	public Date getDateDiag() {
		return this.dateDiag;
	}
	public void setDateDiag(Date dateDiag) {
		this.dateDiag = dateDiag;
	}

	public String getPkEmpDiag() {
		return this.pkEmpDiag;
	}
	public void setPkEmpDiag(String pkEmpDiag) {
		this.pkEmpDiag = pkEmpDiag;
	}

	public String getNameEmpDiag() {
		return this.nameEmpDiag;
	}
	public void setNameEmpDiag(String nameEmpDiag) {
		this.nameEmpDiag = nameEmpDiag;
	}

	public String getFlagFinally() {
		return this.flagFinally;
	}
	public void setFlagFinally(String flagFinally) {
		this.flagFinally = flagFinally;
	}

	public String getFlagCure() {
		return this.flagCure;
	}
	public void setFlagCure(String flagCure) {
		this.flagCure = flagCure;
	}

	public String getModifier() {
		return this.modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getDtTreatway() {
		return this.dtTreatway;
	}
	public void setDtTreatway(String dtTreatway) {
		this.dtTreatway = dtTreatway;
	}

	public Double getVal() {
		return this.val;
	}
	public void setVal(Double val) {
		this.val = val;
	}
	public String getPkFather() {
		return pkFather;
	}
	public void setPkFather(String pkFather) {
		this.pkFather = pkFather;
	}
	public String getNameDiag() {
		return nameDiag;
	}
	public void setNameDiag(String nameDiag) {
		this.nameDiag = nameDiag;
	}
	public String getCodeIcd() {
		return codeIcd;
	}
	public void setCodeIcd(String codeIcd) {
		this.codeIcd = codeIcd;
	}
	public String getEuAdmcon() {
		return euAdmcon;
	}
	public void setEuAdmcon(String euAdmcon) {
		this.euAdmcon = euAdmcon;
	}
	public String getEuSptype() {
		return euSptype;
	}
	public void setEuSptype(String euSptype) {
		this.euSptype = euSptype;
	}
	public String getPkDcdt() {
		return pkDcdt;
	}
	public void setPkDcdt(String pkDcdt) {
		this.pkDcdt = pkDcdt;
	}
	public String getCodeDcdt() {
		return codeDcdt;
	}
	public void setCodeDcdt(String codeDcdt) {
		this.codeDcdt = codeDcdt;
	}
	public String getPkCcdt() {
		return pkCcdt;
	}
	public void setPkCcdt(String pkCcdt) {
		this.pkCcdt = pkCcdt;
	}
	public String getCodeCcdt() {
		return codeCcdt;
	}
	public void setCodeCcdt(String codeCcdt) {
		this.codeCcdt = codeCcdt;
	}
	public String getDescBodypart() {
		return descBodypart;
	}
	public void setDescBodypart(String descBodypart) {
		this.descBodypart = descBodypart;
	}
	public String getDescDrgprop() {
		return descDrgprop;
	}
	public void setDescDrgprop(String descDrgprop) {
		this.descDrgprop = descDrgprop;
	}
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	
}
