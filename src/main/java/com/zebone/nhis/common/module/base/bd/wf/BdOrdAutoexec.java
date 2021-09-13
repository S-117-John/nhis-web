package com.zebone.nhis.common.module.base.bd.wf;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value="BD_ORD_AUTOEXEC")
public class BdOrdAutoexec extends BaseModule {
	
	@PK
	@Field(value="PK_ORDAUTOEXEC",id=KeyId.UUID)
	private String pkOrdautoexec;
	
	@Field(value="PK_DEPT")
	private String pkDept;
	
	/**0 全部，1医嘱类型，2医嘱项目*/
	@Field(value="EU_TYPE")
	private String euType;
	
	@Field(value="CODE_ORDTYPE")
	private String codeOrdtype;
	
	@Field(value="PK_ORD")
	private String pkOrd;
	
	@Field(value="NOTE")
	private String note;
	
	@Field(value="FLAG_ACTIVE")
	private String flagActive;

	public String getPkOrdautoexec() {
		return pkOrdautoexec;
	}

	public void setPkOrdautoexec(String pkOrdautoexec) {
		this.pkOrdautoexec = pkOrdautoexec;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getEuType() {
		return euType;
	}

	public void setEuType(String euType) {
		this.euType = euType;
	}

	public String getCodeOrdtype() {
		return codeOrdtype;
	}

	public void setCodeOrdtype(String codeOrdtype) {
		this.codeOrdtype = codeOrdtype;
	}

	public String getPkOrd() {
		return pkOrd;
	}

	public void setPkOrd(String pkOrd) {
		this.pkOrd = pkOrd;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getFlagActive() {
		return flagActive;
	}

	public void setFlagActive(String flagActive) {
		this.flagActive = flagActive;
	}
	
}
