package com.zebone.nhis.common.module.ex.nis.ns;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


/**
 * Table: EX_LAB_OCC - ex_lab_occ
 *
 * @since 2016-10-28 10:52:00
 */
@Table(value="EX_LAB_OCC")
public class ExLabOcc extends BaseModule  {

	@PK
	@Field(value="PK_LABOCC",id=KeyId.UUID)
    private String pkLabocc;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="CODE_APPLY")
    private String codeApply;

	@Field(value="PK_ORG_OCC")
    private String pkOrgOcc;

	@Field(value="PK_DEPT_OCC")
    private String pkDeptOcc;

	@Field(value="PK_EMP_OCC")
    private String pkEmpOcc;

	@Field(value="NAME_EMP_OCC")
    private String nameEmpOcc;

	@Field(value="DATE_OCC")
    private Date dateOcc;

	@Field(value="DATE_RPT")
    private Date dateRpt;

	@Field(value="FLAG_CHK")
    private String flagChk;

	@Field(value="DATE_CHK")
    private Date dateChk;

	@Field(value="PK_EMP_CHK")
    private String pkEmpChk;

	@Field(value="NAME_EMP_CHK")
    private String nameEmpChk;

	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="SORT_NO")
    private Integer sortNo;

	@Field(value="DATE_COLL")
    private Date dateColl;

	@Field(value="CODE_INDEX")
    private String codeIndex;

	@Field(value="NAME_INDEX")
    private String nameIndex;

	@Field(value="PK_UNIT")
    private String pkUnit;

	@Field(value="UNIT")
    private String unit;

	@Field(value="VAL_MAX")
    private String valMax;

	@Field(value="VAL_MIN")
    private String valMin;

	@Field(value="VAL")
    private String val;

	@Field(value="VAL1")
    private String val1;

	@Field(value="VAL2")
    private String val2;

	@Field(value="VAL3")
    private String val3;

	@Field(value = "VAL4")
	private String val4;

	/**
	 * EU_RESULT - 0正常，1高于，-1低于
	 */
	@Field(value = "EU_RESULT")
	private String euResult;

	@Field(value = "CODE_RPT")
	private String codeRpt;

	@Field(value = "MODIFY_TIME")
	private Date modifyTime;

	@Field(value = "EU_TYPE")
	private String euType;

	@Field(value = "NAME_ORD")
	private String nameOrd;

	@Field(value = "CODE_SAMP")
	private String codeSamp;

    @Field(value="PK_MSP")
	private String pkMsp;


	public String getPkLabocc() {
		return pkLabocc;
	}

	public void setPkLabocc(String pkLabocc) {
		this.pkLabocc = pkLabocc;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getPkCnord() {
		return pkCnord;
	}

	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}

	public String getCodeApply() {
		return codeApply;
	}

	public void setCodeApply(String codeApply) {
		this.codeApply = codeApply;
	}

	public String getPkOrgOcc() {
		return pkOrgOcc;
	}

	public void setPkOrgOcc(String pkOrgOcc) {
		this.pkOrgOcc = pkOrgOcc;
	}

	public String getPkDeptOcc() {
		return pkDeptOcc;
	}

	public void setPkDeptOcc(String pkDeptOcc) {
		this.pkDeptOcc = pkDeptOcc;
	}

	public String getPkEmpOcc() {
		return pkEmpOcc;
	}

	public void setPkEmpOcc(String pkEmpOcc) {
		this.pkEmpOcc = pkEmpOcc;
	}

	public String getNameEmpOcc() {
		return nameEmpOcc;
	}

	public void setNameEmpOcc(String nameEmpOcc) {
		this.nameEmpOcc = nameEmpOcc;
	}

	public Date getDateOcc() {
		return dateOcc;
	}

	public void setDateOcc(Date dateOcc) {
		this.dateOcc = dateOcc;
	}

	public Date getDateRpt() {
		return dateRpt;
	}

	public void setDateRpt(Date dateRpt) {
		this.dateRpt = dateRpt;
	}

	public String getFlagChk() {
		return flagChk;
	}

	public void setFlagChk(String flagChk) {
		this.flagChk = flagChk;
	}

	public Date getDateChk() {
		return dateChk;
	}

	public void setDateChk(Date dateChk) {
		this.dateChk = dateChk;
	}

	public String getPkEmpChk() {
		return pkEmpChk;
	}

	public void setPkEmpChk(String pkEmpChk) {
		this.pkEmpChk = pkEmpChk;
	}

	public String getNameEmpChk() {
		return nameEmpChk;
	}

	public void setNameEmpChk(String nameEmpChk) {
		this.nameEmpChk = nameEmpChk;
	}

	public String getPkOrd() {
		return pkOrd;
	}

	public void setPkOrd(String pkOrd) {
		this.pkOrd = pkOrd;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Date getDateColl() {
		return dateColl;
	}

	public void setDateColl(Date dateColl) {
		this.dateColl = dateColl;
	}

	public String getCodeIndex() {
		return codeIndex;
	}

	public void setCodeIndex(String codeIndex) {
		this.codeIndex = codeIndex;
	}

	public String getNameIndex() {
		return nameIndex;
	}

	public void setNameIndex(String nameIndex) {
		this.nameIndex = nameIndex;
	}

	public String getPkUnit() {
		return pkUnit;
	}

	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getValMax() {
		return valMax;
	}

	public void setValMax(String valMax) {
		this.valMax = valMax;
	}

	public String getValMin() {
		return valMin;
	}

	public void setValMin(String valMin) {
		this.valMin = valMin;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getVal1() {
		return val1;
	}

	public void setVal1(String val1) {
		this.val1 = val1;
	}

	public String getVal2() {
		return val2;
	}

	public void setVal2(String val2) {
		this.val2 = val2;
	}

	public String getVal3() {
		return val3;
	}

	public void setVal3(String val3) {
		this.val3 = val3;
	}

	public String getVal4() {
		return val4;
	}

	public void setVal4(String val4) {
		this.val4 = val4;
	}

	public String getEuResult() {
		return euResult;
	}

	public void setEuResult(String euResult) {
		this.euResult = euResult;
	}

	public String getCodeRpt() {
		return codeRpt;
	}

	public void setCodeRpt(String codeRpt) {
		this.codeRpt = codeRpt;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getEuType() {
		return euType;
	}

	public void setEuType(String euType) {
		this.euType = euType;
	}

	public String getNameOrd() {
		return nameOrd;
	}

	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}

	public String getCodeSamp() {
		return codeSamp;
	}

	public void setCodeSamp(String codeSamp) {
		this.codeSamp = codeSamp;
	}

	public String getPkMsp() {
		return pkMsp;
	}

	public void setPkMsp(String pkMsp) {
		this.pkMsp = pkMsp;
	}

    
}