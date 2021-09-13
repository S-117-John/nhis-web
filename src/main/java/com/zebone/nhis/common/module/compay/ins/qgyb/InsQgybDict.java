package com.zebone.nhis.common.module.compay.ins.qgyb;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value="INS_QGYB_DICT")
public class InsQgybDict extends BaseModule{
	@PK
	@Field(value="PK_INSDICT",id=KeyId.UUID)
    private String pkInsdict;
	/** EU_HPDICTTYPE - 1-全国医保，2-省工伤 */
	@Field(value="EU_HPDICTTYPE")
    private String euHpdicttype;
	/** CODE_TYPE - 字典类型 */
	//@JsonProperty("type")
	@Field(value="CODE_TYPE")
    private String codeType;
	//@JsonProperty("value")
	@Field(value="CODE")
    private String code;
	//@JsonProperty("label")
	@Field(value="NAME")
    private String name;
	@Field(value="SPCODE")
    private String spcode;

	@Field(value="FLAG_DEF")
    private String flagDef;

	@Field(value="NOTE")
    private String note;
	
	/** FLAG_CHD - 0 父记录，1子记录 */
	@Field(value="FLAG_CHD")
    private String flagChd;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODIFIER_TIME")
    private Date modifierTime;

	@Field(value="STOP_FLAG")
    private String stopFlag;
	
	public String getPkInsdict() {
		return pkInsdict;
	}

	public void setPkInsdict(String pkInsdict) {
		this.pkInsdict = pkInsdict;
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

	public String getSpcode() {
		return spcode;
	}

	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}

	public String getFlagDef() {
		return flagDef;
	}

	public void setFlagDef(String flagDef) {
		this.flagDef = flagDef;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getFlagChd() {
		return flagChd;
	}

	public void setFlagChd(String flagChd) {
		this.flagChd = flagChd;
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

	public String getStopFlag() {
		return stopFlag;
	}

	public void setStopFlag(String stopFlag) {
		this.stopFlag = stopFlag;
	}
}
