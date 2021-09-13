package com.zebone.nhis.common.module.emr.rec.dict;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EMR_PARAGRAPH_DC 
 *
 * @since 2017-04-26 03:05:47
 */
@Table(value="EMR_PARAGRAPH_DC")
public class EmrParagraphDc   {

	@PK
	@Field(value="PK_PARA",id=KeyId.UUID)
    private String pkPara;

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

	@Field(value="WS_CODE")
    private String wsCode;

	@Field(value="EU_PARA_TYPE")
    private String euParaType;

	@Field(value="SORT_NUM")
    private Integer sortNum;

	@Field(value="SHORT_NAME")
    private String shortName;

	@Field(value="REMARK")
    private String remark;

	@Field(value="CONTENT_EDITABLE")
    private String contentEditable;

	@Field(value="DELETEABLE")
    private String deleteable;

	@Field(value="VISIBLE")
    private String visible;

	@Field(value="FLAG_EDIT_IN_READ_ONLY")
    private String flagEditInReadOnly;

	@Field(value="FLAG_TITLE_VISIBLE")
    private String flagTitleVisible;

	@Field(value="LABEL_TEXT")
    private String labelText;

	@Field(value="ALIGNMENT")
    private String alignment;

	@Field(value="EU_CAN_EDIT_TITLE_PROP")
    private String euCanEditTitleProp;

	@Field(value="BORDER_VISIBLE")
    private String borderVisible;

	@Field(value="TEXT")
    private byte[] text;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(date=FieldType.ALL)
    private Date ts;

	private String codeStatus;
	
    public String getPkPara(){
        return this.pkPara;
    }
    public void setPkPara(String pkPara){
        this.pkPara = pkPara;
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

    public String getWsCode(){
        return this.wsCode;
    }
    public void setWsCode(String wsCode){
        this.wsCode = wsCode;
    }

    public String getEuParaType(){
        return this.euParaType;
    }
    public void setEuParaType(String euParaType){
        this.euParaType = euParaType;
    }

    public Integer getSortNum(){
        return this.sortNum;
    }
    public void setSortNum(Integer sortNum){
        this.sortNum = sortNum;
    }

    public String getShortName(){
        return this.shortName;
    }
    public void setShortName(String shortName){
        this.shortName = shortName;
    }

    public String getRemark(){
        return this.remark;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }

    public String getContentEditable(){
        return this.contentEditable;
    }
    public void setContentEditable(String contentEditable){
        this.contentEditable = contentEditable;
    }

    public String getDeleteable(){
        return this.deleteable;
    }
    public void setDeleteable(String deleteable){
        this.deleteable = deleteable;
    }

    public String getVisible(){
        return this.visible;
    }
    public void setVisible(String visible){
        this.visible = visible;
    }

    public String getFlagEditInReadOnly(){
        return this.flagEditInReadOnly;
    }
    public void setFlagEditInReadOnly(String flagEditInReadOnly){
        this.flagEditInReadOnly = flagEditInReadOnly;
    }

    public String getFlagTitleVisible(){
        return this.flagTitleVisible;
    }
    public void setFlagTitleVisible(String flagTitleVisible){
        this.flagTitleVisible = flagTitleVisible;
    }

    public String getLabelText(){
        return this.labelText;
    }
    public void setLabelText(String labelText){
        this.labelText = labelText;
    }

    public String getAlignment(){
        return this.alignment;
    }
    public void setAlignment(String alignment){
        this.alignment = alignment;
    }

    public String getEuCanEditTitleProp(){
        return this.euCanEditTitleProp;
    }
    public void setEuCanEditTitleProp(String euCanEditTitleProp){
        this.euCanEditTitleProp = euCanEditTitleProp;
    }

    public String getBorderVisible(){
        return this.borderVisible;
    }
    public void setBorderVisible(String borderVisible){
        this.borderVisible = borderVisible;
    }

    public byte[] getText(){
        return this.text;
    }
    public void setText(byte[] text){
        this.text = text;
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
    
	public String getCodeStatus() {
		return codeStatus;
	}

	public void setCodeStatus(String codeStatus) {
		this.codeStatus = codeStatus;
	}
}