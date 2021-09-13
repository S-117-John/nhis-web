package com.zebone.nhis.common.module.base.bd.srv;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_QC_SCREEN 
 *
 * @since 2019-08-28 
 */
@Table(value="BD_QC_SCREEN")
public class BdQcScreen extends BaseModule  {

	@PK
	@Field(value="PK_QCSCREEN",id=KeyId.UUID)
    private String pkQcscreen;

	@Field(value="COMPUTER")
    private String computer;

	@Field(value="CODE_SCN")
    private Integer codeScn;

	@Field(value="NAME_SCN")
    private String nameScn;

	@Field(value="VOICE")
    private String voice;

	@Field(value="NOTE")
    private String note;

	public String getPkQcscreen() {
		return pkQcscreen;
	}

	public void setPkQcscreen(String pkQcscreen) {
		this.pkQcscreen = pkQcscreen;
	}

	public String getComputer() {
		return computer;
	}

	public void setComputer(String computer) {
		this.computer = computer;
	}

	public Integer getCodeScn() {
		return codeScn;
	}

	public void setCodeScn(Integer codeScn) {
		this.codeScn = codeScn;
	}

	public String getNameScn() {
		return nameScn;
	}

	public void setNameScn(String nameScn) {
		this.nameScn = nameScn;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}