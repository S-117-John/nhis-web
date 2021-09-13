package com.zebone.nhis.common.module.base.bd.mk;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table BD_EHP_QCRULE
 *
 */
@Table(value="BD_EHP_QCRULE")
public class BdEhpQcrule extends BaseModule{
	//主键
	@PK
	@Field(value="PK_QCRULE",id=KeyId.UUID)
	private String pkQcrule;
	//机构
	@Field(value="PK_ORG")
	private String pkOrg;
	//编码
	@Field(value="CODE_RULE")
	private String codeRule;
	//名称
	@Field(value="NAME_RULE")
	private String nameRule;
	//拼音码
	@Field(value="SPCODE")
	private String spcode;
	
	//自定义码
	@Field(value="D_CODE")
	private String dCode;
	//顺序号
	@Field(value="SORTNO")
	private String sortno;
	//规则类型
	@Field(value="EU_TYPE")
	private String euType;
	//启用标志
	@Field(value="FLAG_ACTIVE")
	private String flagActive;
	//备注
	@Field(value="NOTE")
	private String note;
	//事件类型
	@Field(value="DT_CNEVENT")
	private String dtCnevent;
	//质控元素
	@Field(value="DT_EHPITEM")
	private String dtEhpitem;
	//约束类型
	@Field(value="EU_REST")
	private String euRest;
	//运算符号
	@Field(value="EU_OPERATOR")
	private String euOperator;
	//约束值
	@Field(value="VAL")
	private String val;
	//约束单位
	@Field(value="EU_UNIT")
	private String euUnit;
	//预警数值
	@Field(value="VAL_WARNING")
	private String valWarning;
	//预警时间单位
	@Field(value="EU_UNIT_WARNING")
	private String euUnitWarning;
	//约束值上限
	@Field(value="VAL_MAX")
	private String valMax;

	public String getPkQcrule() {
		return pkQcrule;
	}

	public void setPkQcrule(String pkQcrule) {
		this.pkQcrule = pkQcrule;
	}

	@Override
	public String getPkOrg() {
		return pkOrg;
	}

	@Override
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getCodeRule() {
		return codeRule;
	}

	public void setCodeRule(String codeRule) {
		this.codeRule = codeRule;
	}

	public String getNameRule() {
		return nameRule;
	}

	public void setNameRule(String nameRule) {
		this.nameRule = nameRule;
	}

	public String getSpcode() {
		return spcode;
	}

	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}

	public String getdCode() {
		return dCode;
	}

	public void setdCode(String dCode) {
		this.dCode = dCode;
	}

	public String getSortno() {
		return sortno;
	}

	public void setSortno(String sortno) {
		this.sortno = sortno;
	}

	public String getEuType() {
		return euType;
	}

	public void setEuType(String euType) {
		this.euType = euType;
	}

	public String getFlagActive() {
		return flagActive;
	}

	public void setFlagActive(String flagActive) {
		this.flagActive = flagActive;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getDtCnevent() {
		return dtCnevent;
	}

	public void setDtCnevent(String dtCnevent) {
		this.dtCnevent = dtCnevent;
	}

	public String getDtEhpitem() {
		return dtEhpitem;
	}

	public void setDtEhpitem(String dtEhpitem) {
		this.dtEhpitem = dtEhpitem;
	}

	public String getEuRest() {
		return euRest;
	}

	public void setEuRest(String euRest) {
		this.euRest = euRest;
	}

	public String getEuOperator() {
		return euOperator;
	}

	public void setEuOperator(String euOperator) {
		this.euOperator = euOperator;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getEuUnit() {
		return euUnit;
	}

	public void setEuUnit(String euUnit) {
		this.euUnit = euUnit;
	}

	public String getValWarning() {
		return valWarning;
	}

	public void setValWarning(String valWarning) {
		this.valWarning = valWarning;
	}

	public String getEuUnitWarning() {
		return euUnitWarning;
	}

	public void setEuUnitWarning(String euUnitWarning) {
		this.euUnitWarning = euUnitWarning;
	}

	public String getValMax() {
		return valMax;
	}

	public void setValMax(String valMax) {
		this.valMax = valMax;
	}
}
