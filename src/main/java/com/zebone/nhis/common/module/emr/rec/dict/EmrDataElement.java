package com.zebone.nhis.common.module.emr.rec.dict;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: EMR_DATA_ELEMENT
 * 
 * @since 2016-09-28 09:36:25
 */
@Table(value = "EMR_DATA_ELEMENT")
public class EmrDataElement extends BaseModule {

	@PK
	@Field(value = "PK_ELEMENT", id = KeyId.UUID)
	private String pkElement;

	@Field(value = "CODE")
	private String code;

	@Field(value = "NAME")
	private String name;

	@Field(value = "PY_CODE")
	private String pyCode;

	@Field(value = "D_CODE")
	private String dCode;

	/** EU_DATA_TYPE - S1/S3/DT/L */
	@Field(value = "EU_DATA_TYPE")
	private String euDataType;

	/**
	 * EU_PRE_FORMAT - AN..7 AN..50 AN..70 AN..100 AN..200 AN..1000 T/F DT15 N2
	 */
	@Field(value = "EU_PRE_FORMAT")
	private String euPreFormat;

	/**
	 * EU_CTRL_TYPE - 1）基本元素： 控件类型： 1 = Combox 2 = ListBox 3 = TextBox 4 =
	 * CheckBox 5 = NumberBox 6 = MultiListBox 7 = MultiCombox 8 = DateTimeBox 9
	 * = RadioButton
	 * 
	 * 2）复合元素 0
	 */
	@Field(value = "EU_CTRL_TYPE")
	private Integer euCtrlType;

	@Field(value = "WS_CODE")
	private String wsCode;

	/**
	 * EU_CODE_TYPE - 1、业务系统（HIS系统：姓名、性别等
	 * ）：编码code_value中对应数据库view_emr_patient_list中字段 2、系统参数（emr_parameter
	 * )：编码code_value对应emr_paramerter中code字段 3、系统时间 4、医生姓名
	 */
	@Field(value = "EU_CODE_TYPE")
	private String euCodeType;

	@Field(value = "CODE_VALUE")
	private String codeValue;

	/**
	 * EU_DICT_TYPE - 空、普通元素 1、值域字典(取自emr_dict_range;emr_dict_range_code） <下拉列表>
	 * 2、业务字典表（单独字典表icd_10、科室、手术字典等）<文本框>
	 * 3、双击显示<隐藏>：为空时隐藏，有值时显示控件中自定义属性：hidden_text中，控件加入文档时默认值可用dict_code中记录
	 */
	@Field(value = "EU_DICT_TYPE")
	private String euDictType;

	/**
	 * DICT_CODE - dict_type=1：dict_code对应emr_dict_range中range_code
	 * dict_type=2：dict_code对应字典表名称
	 */
	@Field(value = "DICT_CODE")
	private String dictCode;

	@Field(value = "SORT_NUM")
	private Integer sortNum;

	/** DE_VALUE_LIST - code:name;code:name;... */
	@Field(value = "DE_VALUE_LIST")
	private String deValueList;

	@Field(value = "EDGE_STYLE")
	private String edgeStyle;

	@Field(value = "EDGE_STYLE2")
	private String edgeStyle2;

	@Field(value = "FLAG_EDGE")
	private String flagEdge;

	@Field(value = "FLAG_DEL_PROTECT")
	private String flagDelProtect;

	@Field(value = "FLAG_EDIT_PROTECT")
	private String flagEditProtect;

	@Field(value = "HELP_TIP")
	private String helpTip;

	@Field(value = "TITLE")
	private String title;

	@Field(value = "SHORT_NAME")
	private String shortName;

	@Field(value = "FLAG_IS_CTRL_HIDDEN")
	private String flagIsCtrlHidden;

	@Field(value = "PLACE_HOLDER")
	private String placeHolder;

	@Field(value = "TEXT")
	private String text;

	@Field(value = "FLAG_MUST_FILL_CONTENT")
	private String flagMustFillContent;

	@Field(value = "FLAG_PRINT_ABLE")
	private String flagPrintAble;

	@Field(value = "FLAG_IS_NOT_IN_XML")
	private String flagIsNotInXml;

	@Field(value = "FLAG_VIEW_SECRET")
	private String flagViewSecret;

	/** MAX_LEN - 文本框特有属性 */
	@Field(value = "MAX_LEN")
	private Integer maxLen;

	@Field(value = "MAX_VALUE")
	private Double maxValue;

	@Field(value = "MIN_VALUE")
	private Double minValue;

	@Field(value = "PRECI")
	private Integer preci;

	@Field(value = "NUM_UNIT")
	private String numUnit;

	@Field(value = "NUM_ERR_INPUT_INFO")
	private String numErrInputInfo;

	@Field(value = "NUM_OUT_RANGE_INFO")
	private String numOutRangeInfo;

	@Field(value = "CHECK_BOX_STATUS")
	private String checkBoxStatus;

	@Field(value = "CHECK_POS")
	private String checkPos;

	/** EU_DATE_FORMAT - 日期框特有接口 */
	@Field(value = "EU_DATE_FORMAT")
	private Integer euDateFormat;

	@Field(value = "DEFINITION")
	private String definition;

	@Field(value = "FONT_NAME")
	private String fontName;

	/**
	 * EU_FONT_SIZE - 6.5： 小六 7.5： 六号 9： 小五 10.5： 五号 12： 小四 14: 四号 15: 小三 16: 三号
	 * 18: 小二 22： 二号 24： 小一 26： 一号
	 */
	@Field(value = "EU_FONT_SIZE")
	private Integer euFontSize;

	/**
	 * EU_FONT_TYPE - 0 – 正常字体 1 – 斜体 2 - 粗体 3 – 粗斜体
	 */
	@Field(value = "EU_FONT_TYPE")
	private Integer euFontType;

	/**
	 * EU_ALIGN_TYPE - 1 – 左对齐 2 – 居中 3 – 右对齐 4 – 两端对齐
	 */
	@Field(value = "EU_ALIGN_TYPE")
	private Integer euAlignType;

	@Field(value = "MUTEX_STR")
	private String mutexStr;

	@Field(value = "REMARK")
	private String remark;

	@Field(value = "DE_DATA")
	private byte[] deData;

	@Field(value = "IMG_INDEX")
	private Integer imgIndex;
	
	@Field(value = "REPULSION_FOR_GROUP")
	private String repulsionForGroup;
	
	@Field(value = "DE_CODE_VALUE")
	private String deCodeValue;
	
	private String status;

	public String getPkElement() {
		return this.pkElement;
	}

	public void setPkElement(String pkElement) {
		this.pkElement = pkElement;
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

	public String getEuDataType() {
		return this.euDataType;
	}

	public void setEuDataType(String euDataType) {
		this.euDataType = euDataType;
	}

	public String getEuPreFormat() {
		return this.euPreFormat;
	}

	public void setEuPreFormat(String euPreFormat) {
		this.euPreFormat = euPreFormat;
	}

	public Integer getEuCtrlType() {
		return this.euCtrlType;
	}

	public void setEuCtrlType(Integer euCtrlType) {
		this.euCtrlType = euCtrlType;
	}

	public String getWsCode() {
		return this.wsCode;
	}

	public void setWsCode(String wsCode) {
		this.wsCode = wsCode;
	}

	public String getEuCodeType() {
		return this.euCodeType;
	}

	public void setEuCodeType(String euCodeType) {
		this.euCodeType = euCodeType;
	}

	public String getCodeValue() {
		return this.codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

	public String getEuDictType() {
		return this.euDictType;
	}

	public void setEuDictType(String euDictType) {
		this.euDictType = euDictType;
	}

	public String getDictCode() {
		return this.dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public Integer getSortNum() {
		return this.sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public String getDeValueList() {
		return this.deValueList;
	}

	public void setDeValueList(String deValueList) {
		this.deValueList = deValueList;
	}

	public String getEdgeStyle() {
		return this.edgeStyle;
	}

	public void setEdgeStyle(String edgeStyle) {
		this.edgeStyle = edgeStyle;
	}

	public String getEdgeStyle2() {
		return this.edgeStyle2;
	}

	public void setEdgeStyle2(String edgeStyle2) {
		this.edgeStyle2 = edgeStyle2;
	}

	public String getFlagEdge() {
		return this.flagEdge;
	}

	public void setFlagEdge(String flagEdge) {
		this.flagEdge = flagEdge;
	}

	public String getFlagDelProtect() {
		return this.flagDelProtect;
	}

	public void setFlagDelProtect(String flagDelProtect) {
		this.flagDelProtect = flagDelProtect;
	}

	public String getFlagEditProtect() {
		return this.flagEditProtect;
	}

	public void setFlagEditProtect(String flagEditProtect) {
		this.flagEditProtect = flagEditProtect;
	}

	public String getHelpTip() {
		return this.helpTip;
	}

	public void setHelpTip(String helpTip) {
		this.helpTip = helpTip;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getFlagIsCtrlHidden() {
		return this.flagIsCtrlHidden;
	}

	public void setFlagIsCtrlHidden(String flagIsCtrlHidden) {
		this.flagIsCtrlHidden = flagIsCtrlHidden;
	}

	public String getPlaceHolder() {
		return this.placeHolder;
	}

	public void setPlaceHolder(String placeHolder) {
		this.placeHolder = placeHolder;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFlagMustFillContent() {
		return this.flagMustFillContent;
	}

	public void setFlagMustFillContent(String flagMustFillContent) {
		this.flagMustFillContent = flagMustFillContent;
	}

	public String getFlagPrintAble() {
		return this.flagPrintAble;
	}

	public void setFlagPrintAble(String flagPrintAble) {
		this.flagPrintAble = flagPrintAble;
	}

	public String getFlagIsNotInXml() {
		return this.flagIsNotInXml;
	}

	public void setFlagIsNotInXml(String flagIsNotInXml) {
		this.flagIsNotInXml = flagIsNotInXml;
	}

	public String getFlagViewSecret() {
		return this.flagViewSecret;
	}

	public void setFlagViewSecret(String flagViewSecret) {
		this.flagViewSecret = flagViewSecret;
	}

	public Integer getMaxLen() {
		return this.maxLen;
	}

	public void setMaxLen(Integer maxLen) {
		this.maxLen = maxLen;
	}

	public Double getMaxValue() {
		return this.maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public Double getMinValue() {
		return this.minValue;
	}

	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}

	public Integer getPreci() {
		return this.preci;
	}

	public void setPreci(Integer preci) {
		this.preci = preci;
	}

	public String getNumUnit() {
		return this.numUnit;
	}

	public void setNumUnit(String numUnit) {
		this.numUnit = numUnit;
	}

	public String getNumErrInputInfo() {
		return this.numErrInputInfo;
	}

	public void setNumErrInputInfo(String numErrInputInfo) {
		this.numErrInputInfo = numErrInputInfo;
	}

	public String getNumOutRangeInfo() {
		return this.numOutRangeInfo;
	}

	public void setNumOutRangeInfo(String numOutRangeInfo) {
		this.numOutRangeInfo = numOutRangeInfo;
	}

	public String getCheckBoxStatus() {
		return this.checkBoxStatus;
	}

	public void setCheckBoxStatus(String checkBoxStatus) {
		this.checkBoxStatus = checkBoxStatus;
	}

	public String getCheckPos() {
		return this.checkPos;
	}

	public void setCheckPos(String checkPos) {
		this.checkPos = checkPos;
	}

	public Integer getEuDateFormat() {
		return this.euDateFormat;
	}

	public void setEuDateFormat(Integer euDateFormat) {
		this.euDateFormat = euDateFormat;
	}

	public String getDefinition() {
		return this.definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getFontName() {
		return this.fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public Integer getEuFontSize() {
		return this.euFontSize;
	}

	public void setEuFontSize(Integer euFontSize) {
		this.euFontSize = euFontSize;
	}

	public Integer getEuFontType() {
		return this.euFontType;
	}

	public void setEuFontType(Integer euFontType) {
		this.euFontType = euFontType;
	}

	public Integer getEuAlignType() {
		return this.euAlignType;
	}

	public void setEuAlignType(Integer euAlignType) {
		this.euAlignType = euAlignType;
	}

	public String getMutexStr() {
		return this.mutexStr;
	}

	public void setMutexStr(String mutexStr) {
		this.mutexStr = mutexStr;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public byte[] getDeData() {
		return this.deData;
	}

	public void setDeData(byte[] deData) {
		this.deData = deData;
	}

	public Integer getImgIndex() {
		return this.imgIndex;
	}

	public void setImgIndex(Integer imgIndex) {
		this.imgIndex = imgIndex;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRepulsionForGroup() {
		return repulsionForGroup;
	}

	public void setRepulsionForGroup(String repulsionForGroup) {
		this.repulsionForGroup = repulsionForGroup;
	}

	public String getDeCodeValue() {
		return deCodeValue;
	}

	public void setDeCodeValue(String deCodeValue) {
		this.deCodeValue = deCodeValue;
	}
	

}