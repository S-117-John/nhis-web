package com.zebone.nhis.compay.ins.zsrm.vo.base;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value="INS_QGYB_DICTMAP")
public class InsQgybDictMap extends BaseModule  {

	@PK
	@Field(value="PK_INSDICTMAP",id=KeyId.UUID)
    private String pkInsdictmap;

    /** EU_HPDICTTYPE - 01=全国医保，02=省工伤 */
	@Field(value="EU_HPDICTTYPE")
    private String euHpdicttype;

	@Field(value="CODE_TYPE")
    private String codeType;

	@Field(value="NAME")
    private String name;

	@Field(value="PK_HIS")
    private String pkHis;

	@Field(value="CODE_HIS")
    private String codeHis;

	@Field(value="NAME_HIS")
    private String nameHis;

	@Field(value="CODE_INSUR")
    private String codeInsur;

	@Field(value="NAME_INSUR")
    private String nameInsur;

    /** FLAG_CHD - 0 父目录，1 子目录 */
	@Field(value="FLAG_CHD")
    private String flagChd;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODIFIER_TIME")
    private Date modifierTime;

	@Field(value="FLAG_STOP")
    private String flagStop;
	
	@Field(value="NAME_SOURCE")
	private String nameSource;
	
	@Field(value="CODE_SOURCE")
	private String codeSource;

	public String getPkInsdictmap() {
		return pkInsdictmap;
	}

	public void setPkInsdictmap(String pkInsdictmap) {
		this.pkInsdictmap = pkInsdictmap;
	}

	public String getEuHpdicttype() {
		return euHpdicttype;
	}

	public void setEuHpdicttype(String euHpdicttype) {
		this.euHpdicttype = euHpdicttype;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPkHis() {
		return pkHis;
	}

	public void setPkHis(String pkHis) {
		this.pkHis = pkHis;
	}

	public String getCodeHis() {
		return codeHis;
	}

	public void setCodeHis(String codeHis) {
		this.codeHis = codeHis;
	}

	public String getNameHis() {
		return nameHis;
	}

	public void setNameHis(String nameHis) {
		this.nameHis = nameHis;
	}

	public String getCodeInsur() {
		return codeInsur;
	}

	public void setCodeInsur(String codeInsur) {
		this.codeInsur = codeInsur;
	}

	public String getNameInsur() {
		return nameInsur;
	}

	public void setNameInsur(String nameInsur) {
		this.nameInsur = nameInsur;
	}

	public String getFlagChd() {
		return flagChd;
	}

	public void setFlagChd(String flagChd) {
		this.flagChd = flagChd;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getModifierTime() {
		return modifierTime;
	}

	public void setModifierTime(Date modifierTime) {
		this.modifierTime = modifierTime;
	}

	public String getFlagStop() {
		return flagStop;
	}

	public void setFlagStop(String flagStop) {
		this.flagStop = flagStop;
	}

	public String getNameSource() {
		return nameSource;
	}

	public void setNameSource(String nameSource) {
		this.nameSource = nameSource;
	}

	public String getCodeSource() {
		return codeSource;
	}

	public void setCodeSource(String codeSource) {
		this.codeSource = codeSource;
	}
	

}
