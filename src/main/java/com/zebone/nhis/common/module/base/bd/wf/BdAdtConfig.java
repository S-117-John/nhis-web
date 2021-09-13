package com.zebone.nhis.common.module.base.bd.wf;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: BD_ADT_CONFIG  - bd_adt_config
 * @since 2017-09-12
 */
@Table(value="BD_ADT_CONFIG")
public class BdAdtConfig extends BaseModule{
	
	@PK
	@Field(value="PK_ADTCONFIG",id=KeyId.UUID)
    private String pkAdtconfig;
	   
	@Field(value="DT_ADTCONFTYPE")
    private String dtAdtconftype;
	
	@Field(value="PK_DEPT")
    private String pkDept;
	   
	@Field(value="PK_DEPT_REL")
    private String pkDeptRel;
	
	@Field(value="NOTE")
    private String note;

	public String getPkAdtconfig() {
		return pkAdtconfig;
	}

	public void setPkAdtconfig(String pkAdtconfig) {
		this.pkAdtconfig = pkAdtconfig;
	}

	public String getDtAdtconftype() {
		return dtAdtconftype;
	}

	public void setDtAdtconftype(String dtAdtconftype) {
		this.dtAdtconftype = dtAdtconftype;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getPkDeptRel() {
		return pkDeptRel;
	}

	public void setPkDeptRel(String pkDeptRel) {
		this.pkDeptRel = pkDeptRel;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
