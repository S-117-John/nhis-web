package com.zebone.nhis.common.module.base.bd.wf;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value="BD_PIVASRULE")
public class BdPivasrule extends BaseModule{

	@PK
	@Field(value="PK_PIVASRULE",id=KeyId.UUID)
	private String pkPivasrule;
	
	@Field(value="CODE_RULE")
	private String codeRule;
	
	@Field(value="NAME_RULE")
	private String nameRule;
	
	@Field(value="WHERESQL")
	private String wheresql;
	
	@Field(value="NOTE")
	private String note;

	public String getPkPivasrule() {
		return pkPivasrule;
	}

	public void setPkPivasrule(String pkPivasrule) {
		this.pkPivasrule = pkPivasrule;
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

	public String getWheresql() {
		return wheresql;
	}

	public void setWheresql(String wheresql) {
		this.wheresql = wheresql;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	
}
