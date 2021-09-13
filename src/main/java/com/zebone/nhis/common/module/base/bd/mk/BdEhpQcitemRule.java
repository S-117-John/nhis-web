package com.zebone.nhis.common.module.base.bd.mk;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table BD_EHP_QCITEM_RULE
 *
 */
@Table(value="BD_EHP_QCITEM_RULE")
public class BdEhpQcitemRule extends BaseModule{
	//主键
	@PK
	@Field(value="PK_QCITEMRULE",id=KeyId.UUID)
	private String pkQcitemrule;
	//机构
	@Field(value="PK_ORG")
	private String pkOrg;
	//编码
	@Field(value="CODE_RULE")
	private String codeRule;
	//名称
	@Field(value="NAME_RULE")
	private String nameRule;

	@Field(value="PK_QCITEM")
	private String pkQcitem;

	@Field(value="PK_QCRULE")
	private String pkQcrule;
	
	//类型
	@Field(value="EU_TYPE")
	private String euType;

	public String getPkQcrule() {
		return pkQcrule;
	}

	public void setPkQcrule(String pkQcrule) {
		this.pkQcrule = pkQcrule;
	}

	public String getPkQcitemrule() {
		return pkQcitemrule;
	}

	public void setPkQcitemrule(String pkQcitemrule) {
		this.pkQcitemrule = pkQcitemrule;
	}

	public String getNameRule() {
		return nameRule;
	}

	public void setNameRule(String nameRule) {
		this.nameRule = nameRule;
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

	public String getEuType() {
		return euType;
	}

	public void setEuType(String euType) {
		this.euType = euType;
	}

	public String getPkQcitem() {
		return pkQcitem;
	}

	public void setPkQcitem(String pkQcitem) {
		this.pkQcitem = pkQcitem;
	}
}
