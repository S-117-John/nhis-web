package com.zebone.nhis.common.module.base.bd.wf;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="BD_FLOW")
public class BdFlow extends BaseModule {

	@PK
	@Field(value="PK_FLOW",id=KeyId.UUID)
    private String pkFlow;
	
	@Field(value="CODE_FLOW")
	private String codeFlow;
	
	@Field(value="NAME_FLOW")
	private String nameFlow;
	
	@Field(value="OBJECT_FLOW")
	private String objectFlow;
	
	@Field(value="SPCODE")
	private String spcode;
	
	@Field(value="D_CODE")
	private String dCode;
	
	@Field(value="EU_TYPE")
	private String euType;
	
	@Field(value="FLAG_ACTIVE")
	private String flagActive;
	
	@Field(value="note")
	private String note;

	public String getPkFlow() {
		return pkFlow;
	}

	public void setPkFlow(String pkFlow) {
		this.pkFlow = pkFlow;
	}

	public String getCodeFlow() {
		return codeFlow;
	}

	public void setCodeFlow(String codeFlow) {
		this.codeFlow = codeFlow;
	}

	public String getNameFlow() {
		return nameFlow;
	}

	public void setNameFlow(String nameFlow) {
		this.nameFlow = nameFlow;
	}

	public String getObjectFlow() {
		return objectFlow;
	}

	public void setObjectFlow(String objectFlow) {
		this.objectFlow = objectFlow;
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
	
	
}
