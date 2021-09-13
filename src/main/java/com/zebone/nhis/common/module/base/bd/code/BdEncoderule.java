package com.zebone.nhis.common.module.base.bd.code;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: BD_ENCODERULE - bd_encoderule
 *
 * @since 2017-08-05 09:10:51
 */
@Table(value = "BD_ENCODERULE")
public class BdEncoderule extends BaseModule {

	@PK
	@Field(value = "PK_ENCODERULE", id = KeyId.UUID)
	private String pkEncoderule;

	@Field(value = "CODE")
	private String code;

	@Field(value = "NAME")
	private String name;

	@Field("LENGTH_CODE")
	private int lengthCode;

	@Field(value = "FLAG_PREFIX")
	private String flagPrefix;

	@Field(value = "PREFIX")
	private String prefix;

	@Field(value = "FLAG_DATE")
	private String flagDate;

	@Field(value = "FORMAT_DATE")
	private String formatDate;

	/** EU_SNRULE - 0自然增长，1按日归零，2，按月归零，3按年归零 */
	@Field(value = "EU_SNRULE")
	private String euSnrule;

	@Field("VAL_INIT")
	private int valInit;
	
	@Field("VAL")
	private long val;

	@Field(value = "NOTE")
	private String note;

	/** FLAG_SYS - 为true表示系统内置，不可修改 */
	@Field(value = "FLAG_SYS")
	private String flagSys;

	@Field(value = "MODIFIER")
	private String modifier;

	@Field(value = "flag_suffix")
	private String flagSuffix;

	@Field(value = "suffix")
	private String suffix;

	public String getPkEncoderule() {
		return this.pkEncoderule;
	}

	public void setPkEncoderule(String pkEncoderule) {
		this.pkEncoderule = pkEncoderule;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLengthCode() {
		return lengthCode;
	}

	public void setLengthCode(int lengthCode) {
		this.lengthCode = lengthCode;
	}

	public String getFlagPrefix() {
		return this.flagPrefix;
	}

	public void setFlagPrefix(String flagPrefix) {
		this.flagPrefix = flagPrefix;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getFlagDate() {
		return this.flagDate;
	}

	public void setFlagDate(String flagDate) {
		this.flagDate = flagDate;
	}

	public String getFormatDate() {
		return this.formatDate;
	}

	public void setFormatDate(String formatDate) {
		this.formatDate = formatDate;
	}

	public String getEuSnrule() {
		return this.euSnrule;
	}

	public void setEuSnrule(String euSnrule) {
		this.euSnrule = euSnrule;
	}

	public int getValInit() {
		return valInit;
	}

	public void setValInit(int valInit) {
		this.valInit = valInit;
	}

	public long getVal() {
		return val;
	}

	public void setVal(long val) {
		this.val = val;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getFlagSys() {
		return this.flagSys;
	}

	public void setFlagSys(String flagSys) {
		this.flagSys = flagSys;
	}

	public String getModifier() {
		return this.modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getFlagSuffix() {
		return this.flagSuffix;
	}

	public void setFlagSuffix(String flagSuffix) {
		this.flagSuffix = flagSuffix;
	}

	public String getSuffix() {
		return this.suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
}