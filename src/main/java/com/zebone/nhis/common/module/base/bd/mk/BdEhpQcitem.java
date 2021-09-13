package com.zebone.nhis.common.module.base.bd.mk;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table BD_EHP_QCITEM
 *
 */
@Table(value="BD_EHP_QCITEM")
public class BdEhpQcitem extends BaseModule{
	//主键
	@PK
	@Field(value="PK_QCITEM",id=KeyId.UUID)
	private String pkQcitem;
	//机构
	@Field(value="PK_ORG")
	private String pkOrg;
	//编码
	@Field(value="CODE_QC")
	private String codeQc;
	//名称
	@Field(value="NAME_QC")
	private String nameQc;
	//拼音码
	@Field(value="SPCODE")
	private String spcode;
	
	//自定义码
	@Field(value="D_CODE")
	private String dCode;
	//顺序号
	@Field(value="SORTNO")
	private String sortno;
	//所属检查项目
	@Field(value="DT_EHPITEMTYPE")
	private String dtEhpitemtype;
	//所属类别
	@Field(value="EU_ITEMCATE")
	private String euItemcate;
	//备注
	@Field(value="NOTE")
	private String note;
	//扣分标准
	@Field(value="SPEC")
	private String spec;
	//扣分类型
	@Field(value="EU_GDTYPE")
	private String euGdtype;
	//分值
	@Field(value="VAL")
	private String val;
	//扣分上限
	@Field(value="VAL_MAX")
	private String valMax;

	@Field(value="FLAG_ACTIVE")
	private String flagActive;

	public String getFlagActive() {
		return flagActive;
	}

	public void setFlagActive(String flagActive) {
		this.flagActive = flagActive;
	}

	public String getPkQcitem() {
		return pkQcitem;
	}

	public void setPkQcitem(String pkQcitem) {
		this.pkQcitem = pkQcitem;
	}

	@Override
	public String getPkOrg() {
		return pkOrg;
	}

	@Override
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getCodeQc() {
		return codeQc;
	}

	public void setCodeQc(String codeQc) {
		this.codeQc = codeQc;
	}

	public String getNameQc() {
		return nameQc;
	}

	public void setNameQc(String nameQc) {
		this.nameQc = nameQc;
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

	public String getDtEhpitemtype() {
		return dtEhpitemtype;
	}

	public void setDtEhpitemtype(String dtEhpitemtype) {
		this.dtEhpitemtype = dtEhpitemtype;
	}

	public String getEuItemcate() {
		return euItemcate;
	}

	public void setEuItemcate(String euItemcate) {
		this.euItemcate = euItemcate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getEuGdtype() {
		return euGdtype;
	}

	public void setEuGdtype(String euGdtype) {
		this.euGdtype = euGdtype;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getValMax() {
		return valMax;
	}

	public void setValMax(String valMax) {
		this.valMax = valMax;
	}
}
