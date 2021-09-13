package com.zebone.nhis.common.module.emr.rec.dict;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: EMR_PARAGRAPH
 * 
 * @since 2016-09-27 02:33:17
 */
@Table(value = "EMR_PARAGRAPH")
public class EmrParagraph extends BaseModule {

	@PK
	@Field(value = "PK_PARA", id = KeyId.UUID)
	private String pkPara;

	@Field(value = "CODE")
	private String code;

	@Field(value = "NAME")
	private String name;

	@Field(value = "PY_CODE")
	private String pyCode;

	@Field(value = "D_CODE")
	private String dCode;

	@Field(value = "WS_CODE")
	private String wsCode;

	/**
	 * EU_PARA_TYPE - H：文档头 S：文档体
	 */
	@Field(value = "EU_PARA_TYPE")
	private String euParaType;

	@Field(value = "SORT_NUM")
	private Integer sortNum;

	@Field(value = "SHORT_NAME")
	private String shortName;

	@Field(value = "REMARK")
	private String remark;

	@Field(value = "FLAG_EDIT_PROTECT")
	private String flagEditProtect;

	@Field(value = "FLAG_DELETE_PROTECT")
	private String flagDeleteProtect;

	@Field(value = "FLAG_VISIBLE")
	private String flagVisible;

	@Field(value = "FLAG_EDIT_IN_READ_ONLY")
	private String flagEditInReadOnly;

	@Field(value = "FLAG_TITLE_VISIBLE")
	private String flagTitleVisible;

	@Field(value = "TITLE")
	private String title;

	/**
	 * EU_TITLE_POSITION - 1：区域首行居左 2：区域首行居中
	 */
	@Field(value = "EU_TITLE_POSITION")
	private String euTitlePosition;

	@Field(value = "EU_CAN_EDIT_TITLE_PROP")
	private String euCanEditTitleProp;

	@Field(value = "EU_PARA_VISIBLE")
	private String euParaVisible;

	@Field(value = "DOC_DATA")
	private byte[] docData;

	private String codeStatus;

	public String getPkPara() {
		return this.pkPara;
	}

	public void setPkPara(String pkPara) {
		this.pkPara = pkPara;
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

	public String getPyCode() {
		return this.pyCode;
	}

	public void setPyCode(String pyCode) {
		this.pyCode = pyCode;
	}

	public String getdCode() {
		return this.dCode;
	}

	public void setdCode(String dCode) {
		this.dCode = dCode;
	}

	public String getWsCode() {
		return this.wsCode;
	}

	public void setWsCode(String wsCode) {
		this.wsCode = wsCode;
	}

	public String getEuParaType() {
		return this.euParaType;
	}

	public void setEuParaType(String euParaType) {
		this.euParaType = euParaType;
	}

	public Integer getSortNum() {
		return this.sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFlagEditProtect() {
		return this.flagEditProtect;
	}

	public void setFlagEditProtect(String flagEditProtect) {
		this.flagEditProtect = flagEditProtect;
	}

	public String getFlagDeleteProtect() {
		return this.flagDeleteProtect;
	}

	public void setFlagDeleteProtect(String flagDeleteProtect) {
		this.flagDeleteProtect = flagDeleteProtect;
	}

	public String getFlagVisible() {
		return this.flagVisible;
	}

	public void setFlagVisible(String flagVisible) {
		this.flagVisible = flagVisible;
	}

	public String getFlagEditInReadOnly() {
		return this.flagEditInReadOnly;
	}

	public void setFlagEditInReadOnly(String flagEditInReadOnly) {
		this.flagEditInReadOnly = flagEditInReadOnly;
	}

	public String getFlagTitleVisible() {
		return this.flagTitleVisible;
	}

	public void setFlagTitleVisible(String flagTitleVisible) {
		this.flagTitleVisible = flagTitleVisible;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEuTitlePosition() {
		return this.euTitlePosition;
	}

	public void setEuTitlePosition(String euTitlePosition) {
		this.euTitlePosition = euTitlePosition;
	}

	public String getEuCanEditTitleProp() {
		return this.euCanEditTitleProp;
	}

	public void setEuCanEditTitleProp(String euCanEditTitleProp) {
		this.euCanEditTitleProp = euCanEditTitleProp;
	}

	public String getEuParaVisible() {
		return this.euParaVisible;
	}

	public void setEuParaVisible(String euParaVisible) {
		this.euParaVisible = euParaVisible;
	}

	public byte[] getDocData() {
		return this.docData;
	}

	public void setDocData(byte[] docData) {
		this.docData = docData;
	}

	public String getCodeStatus() {
		return codeStatus;
	}

	public void setCodeStatus(String codeStatus) {
		this.codeStatus = codeStatus;
	}

}