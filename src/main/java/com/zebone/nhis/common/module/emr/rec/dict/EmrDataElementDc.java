package com.zebone.nhis.common.module.emr.rec.dict;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EMR_DATA_ELEMENT_DC 
 *
 * @since 2017-04-25 10:52:14
 */
@Table(value="EMR_DATA_ELEMENT_DC")
public class EmrDataElementDc   {

	@PK
	@Field(value="PK_ELEMENT",id=KeyId.UUID)
    private String pkElement;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="PY_CODE")
    private String pyCode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="EU_DATA_TYPE")
    private String euDataType;

	@Field(value="EU_PRE_FORMAT")
    private String euPreFormat;

	@Field(value="EU_CTRL_TYPE")
    private Integer euCtrlType;

	@Field(value="WS_CODE")
    private String wsCode;

	@Field(value="EU_CODE_TYPE")
    private String euCodeType;

	@Field(value="CODE_VALUE")
    private String codeValue;

	@Field(value="EU_DICT_TYPE")
    private String euDictType;

	@Field(value="DICT_CODE")
    private String dictCode;

	@Field(value="SORT_NUM")
    private Integer sortNum;

	@Field(value="DE_VALUE_LIST")
    private String deValueList;

	@Field(value="START_BORDER_TEXT")
    private String startBorderText;

	@Field(value="END_BORDER_TEXT")
    private String endBorderText;

	@Field(value="BORDER_VISIBLE")
    private String borderVisible;

	@Field(value="DELETEABLE")
    private String deleteable;

	@Field(value="CONTENT_EDITABLE")
    private String contentEditable;

	@Field(value="TOOL_TIP")
    private String toolTip;

	@Field(value="LABEL_TEXT")
    private String labelText;

	@Field(value="SHORT_NAME")
    private String shortName;

	@Field(value="VISIBLE")
    private String visible;

	@Field(value="PLACE_HOLDER")
    private String placeHolder;

	@Field(value="TEXT")
    private String text;

	@Field(value="REQIURED")
    private String reqiured;

	@Field(value="PRINT_VISIBILITY")
    private String printVisibility;

	@Field(value="FLAG_IS_NOT_IN_XML")
    private String flagIsNotInXml;

	@Field(value="VIEW_ENCRYPT_TYPE")
    private String viewEncryptType;

	@Field(value="MAX_LENGTH")
    private Integer maxLength;

	@Field(value="MAX_VALUE")
    private Double maxValue;

	@Field(value="MIN_VALUE")
    private Double minValue;

	@Field(value="PRECISION")
    private Integer precision;

	@Field(value="UNIT_TEXT")
    private String unitText;

	@Field(value="NUM_ERR_INPUT_INFO")
    private String numErrInputInfo;

	@Field(value="NUM_OUT_RANGE_INFO")
    private String numOutRangeInfo;

	@Field(value="RUNTIME_SELECTABLE")
    private String runtimeSelectable;

	@Field(value="CHECK_ALIGN_LEFT")
    private String checkAlignLeft;

	@Field(value="VALUE_TYPE")
    private Integer valueType;

	@Field(value="DEFINITION")
    private String definition;

	@Field(value="FONT_NAME")
    private String fontName;

	@Field(value="FONT_SIZE")
    private Integer fontSize;

	@Field(value="FONT")
    private Integer font;

	@Field(value="ALIGNMENT")
    private Integer alignment;

	@Field(value="REPULSION_FOR_GROUP")
    private String repulsionForGroup;

	@Field(value="REMARK")
    private String remark;

	@Field(value="INNER_TEXT")
    private byte[] innerText;

	@Field(value="IMG_INDEX")
    private Integer imgIndex;
	
	private String status;
	
	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkElement(){
        return this.pkElement;
    }
    public void setPkElement(String pkElement){
        this.pkElement = pkElement;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getPyCode(){
        return this.pyCode;
    }
    public void setPyCode(String pyCode){
        this.pyCode = pyCode;
    }

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
    }

    public String getEuDataType(){
        return this.euDataType;
    }
    public void setEuDataType(String euDataType){
        this.euDataType = euDataType;
    }

    public String getEuPreFormat(){
        return this.euPreFormat;
    }
    public void setEuPreFormat(String euPreFormat){
        this.euPreFormat = euPreFormat;
    }

    public Integer getEuCtrlType(){
        return this.euCtrlType;
    }
    public void setEuCtrlType(Integer euCtrlType){
        this.euCtrlType = euCtrlType;
    }

    public String getWsCode(){
        return this.wsCode;
    }
    public void setWsCode(String wsCode){
        this.wsCode = wsCode;
    }

    public String getEuCodeType(){
        return this.euCodeType;
    }
    public void setEuCodeType(String euCodeType){
        this.euCodeType = euCodeType;
    }

    public String getCodeValue(){
        return this.codeValue;
    }
    public void setCodeValue(String codeValue){
        this.codeValue = codeValue;
    }

    public String getEuDictType(){
        return this.euDictType;
    }
    public void setEuDictType(String euDictType){
        this.euDictType = euDictType;
    }

    public String getDictCode(){
        return this.dictCode;
    }
    public void setDictCode(String dictCode){
        this.dictCode = dictCode;
    }

    public Integer getSortNum(){
        return this.sortNum;
    }
    public void setSortNum(Integer sortNum){
        this.sortNum = sortNum;
    }

    public String getDeValueList(){
        return this.deValueList;
    }
    public void setDeValueList(String deValueList){
        this.deValueList = deValueList;
    }

    public String getStartBorderText(){
        return this.startBorderText;
    }
    public void setStartBorderText(String startBorderText){
        this.startBorderText = startBorderText;
    }

    public String getEndBorderText(){
        return this.endBorderText;
    }
    public void setEndBorderText(String endBorderText){
        this.endBorderText = endBorderText;
    }

    public String getBorderVisible(){
        return this.borderVisible;
    }
    public void setBorderVisible(String borderVisible){
        this.borderVisible = borderVisible;
    }

    public String getDeleteable(){
        return this.deleteable;
    }
    public void setDeleteable(String deleteable){
        this.deleteable = deleteable;
    }

    public String getContentEditable(){
        return this.contentEditable;
    }
    public void setContentEditable(String contentEditable){
        this.contentEditable = contentEditable;
    }

    public String getToolTip(){
        return this.toolTip;
    }
    public void setToolTip(String toolTip){
        this.toolTip = toolTip;
    }

    public String getLabelText(){
        return this.labelText;
    }
    public void setLabelText(String labelText){
        this.labelText = labelText;
    }

    public String getShortName(){
        return this.shortName;
    }
    public void setShortName(String shortName){
        this.shortName = shortName;
    }

    public String getVisible(){
        return this.visible;
    }
    public void setVisible(String visible){
        this.visible = visible;
    }

    public String getPlaceHolder(){
        return this.placeHolder;
    }
    public void setPlaceHolder(String placeHolder){
        this.placeHolder = placeHolder;
    }

    public String getText(){
        return this.text;
    }
    public void setText(String text){
        this.text = text;
    }

    public String getReqiured(){
        return this.reqiured;
    }
    public void setReqiured(String reqiured){
        this.reqiured = reqiured;
    }

    public String getPrintVisibility(){
        return this.printVisibility;
    }
    public void setPrintVisibility(String printVisibility){
        this.printVisibility = printVisibility;
    }

    public String getFlagIsNotInXml(){
        return this.flagIsNotInXml;
    }
    public void setFlagIsNotInXml(String flagIsNotInXml){
        this.flagIsNotInXml = flagIsNotInXml;
    }

    public String getViewEncryptType(){
        return this.viewEncryptType;
    }
    public void setViewEncryptType(String viewEncryptType){
        this.viewEncryptType = viewEncryptType;
    }

    public Integer getMaxLength(){
        return this.maxLength;
    }
    public void setMaxLength(Integer maxLength){
        this.maxLength = maxLength;
    }

    public Double getMaxValue(){
        return this.maxValue;
    }
    public void setMaxValue(Double maxValue){
        this.maxValue = maxValue;
    }

    public Double getMinValue(){
        return this.minValue;
    }
    public void setMinValue(Double minValue){
        this.minValue = minValue;
    }

    public Integer getPrecision(){
        return this.precision;
    }
    public void setPrecision(Integer precision){
        this.precision = precision;
    }

    public String getUnitText(){
        return this.unitText;
    }
    public void setUnitText(String unitText){
        this.unitText = unitText;
    }

    public String getNumErrInputInfo(){
        return this.numErrInputInfo;
    }
    public void setNumErrInputInfo(String numErrInputInfo){
        this.numErrInputInfo = numErrInputInfo;
    }

    public String getNumOutRangeInfo(){
        return this.numOutRangeInfo;
    }
    public void setNumOutRangeInfo(String numOutRangeInfo){
        this.numOutRangeInfo = numOutRangeInfo;
    }

    public String getRuntimeSelectable(){
        return this.runtimeSelectable;
    }
    public void setRuntimeSelectable(String runtimeSelectable){
        this.runtimeSelectable = runtimeSelectable;
    }

    public String getCheckAlignLeft(){
        return this.checkAlignLeft;
    }
    public void setCheckAlignLeft(String checkAlignLeft){
        this.checkAlignLeft = checkAlignLeft;
    }

    public Integer getValueType(){
        return this.valueType;
    }
    public void setValueType(Integer valueType){
        this.valueType = valueType;
    }

    public String getDefinition(){
        return this.definition;
    }
    public void setDefinition(String definition){
        this.definition = definition;
    }

    public String getFontName(){
        return this.fontName;
    }
    public void setFontName(String fontName){
        this.fontName = fontName;
    }

    public Integer getFontSize(){
        return this.fontSize;
    }
    public void setFontSize(Integer fontSize){
        this.fontSize = fontSize;
    }

    public Integer getFont(){
        return this.font;
    }
    public void setFont(Integer font){
        this.font = font;
    }

    public Integer getAlignment(){
        return this.alignment;
    }
    public void setAlignment(Integer alignment){
        this.alignment = alignment;
    }

    public String getRepulsionForGroup(){
        return this.repulsionForGroup;
    }
    public void setRepulsionForGroup(String repulsionForGroup){
        this.repulsionForGroup = repulsionForGroup;
    }

    public String getRemark(){
        return this.remark;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }

    public byte[] getInnerText(){
        return this.innerText;
    }
    public void setInnerText(byte[] innerText){
        this.innerText = innerText;
    }

    public Integer getImgIndex(){
        return this.imgIndex;
    }
    public void setImgIndex(Integer imgIndex){
        this.imgIndex = imgIndex;
    }
    
    
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
    
    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }

    public String getCreator(){
        return this.creator;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }

    public Date getCreateTime(){
        return this.createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
}
