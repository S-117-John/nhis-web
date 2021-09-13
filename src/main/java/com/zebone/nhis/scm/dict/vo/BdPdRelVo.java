package com.zebone.nhis.scm.dict.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value="BD_PD_REL")
public class BdPdRelVo extends BaseModule{
	@PK
	@Field(value="PK_PDREL",id=KeyId.UUID)
	private String pkPdrel;
	
	@Field(value="PK_PD")
	private String pkPd;
	
	@Field(value="PK_PD_REL")
	private String pkPdRel;
	
	@Field(value="EU_RELTYPE")
	private String euReltype;
	
	@Field(value="SORTNO")
	private String sortno;
	
	@Field(value="NOTE")
	private String note;
	
	private String code;
	
	private String name;
	
	private String spec;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getPkPdrel() {
		return pkPdrel;
	}

	public void setPkPdrel(String pkPdrel) {
		this.pkPdrel = pkPdrel;
	}


	public String getPkPd() {
		return pkPd;
	}

	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}

	public String getPkPdRel() {
		return pkPdRel;
	}

	public void setPkPdRel(String pkPdRel) {
		this.pkPdRel = pkPdRel;
	}

	public String getEuReltype() {
		return euReltype;
	}

	public void setEuReltype(String euReltype) {
		this.euReltype = euReltype;
	}

	public String getSortno() {
		return sortno;
	}

	public void setSortno(String sortno) {
		this.sortno = sortno;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	
}
