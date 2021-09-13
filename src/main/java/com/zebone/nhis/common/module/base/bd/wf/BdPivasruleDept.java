package com.zebone.nhis.common.module.base.bd.wf;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value="BD_PIVASRULE_DEPT")
public class BdPivasruleDept extends BaseModule{

	@PK
	@Field(value="PK_PIVASRULEDEPT",id=KeyId.UUID)
	private String pkPivasruledept;
	
	@Field(value="PK_PIVASRULE")
	private String pkPivasrule;
	
	@Field(value="PK_DEPT")
	private String pkDept;
	
	@Field(value="NOTE")
	private String note;

	public String getPkPivasruledept() {
		return pkPivasruledept;
	}

	public void setPkPivasruledept(String pkPivasruledept) {
		this.pkPivasruledept = pkPivasruledept;
	}
	
	public String getPkPivasrule() {
		return pkPivasrule;
	}

	public void setPkPivasrule(String pkPivasrule) {
		this.pkPivasrule = pkPivasrule;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
}
