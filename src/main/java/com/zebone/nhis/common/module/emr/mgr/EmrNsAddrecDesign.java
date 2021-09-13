package com.zebone.nhis.common.module.emr.mgr;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EMR_NS_ADDREC_DESIGN 
 *
 * @since 2018-01-19 02:57:00
 */
@Table(value="EMR_NS_ADDREC_DESIGN")
public class EmrNsAddrecDesign extends BaseModule  {

	@Field(value="PK_ADDREC_CONFIG")
    private String pkAddrecConfig;

	@Field(value="GROUP_NAME")
    private String groupName;

	@Field(value="COLUMN_TEXT")
    private String columnText;

	@Field(value="UNIT")
    private String unit;

	@Field(value="COLUMN_TYPE")
    private String columnType;

	@Field(value="FORMAT")
    private String format;

	@Field(value="COLUMN_NAME")
    private String columnName;

	@Field(value="DEFAULT_VALUE")
    private String defaultValue;

	@Field(value="FIELD")
    private String field;

	@Field(value="FIELD_TYPE")
    private String fieldType;

	@Field(value="DT_CODE")
    private String dtCode;

	@Field(value="VISIBLE")
    private String visible;

	/*@Field(value="UPDATE")
    private String update;*/

	@Field(value="ALLOWNULL")
    private String allownull;

	@Field(value="X")
    private String x;

	@Field(value="Y")
    private String y;

	@Field(value="WIDTH")
    private Double width;

	@Field(value="HEIGHT")
    private Double height;

	@Field(value="ATTRIBUTE")
    private String attribute;

	@Field(value="EVENT")
    private String event;

	@Field(value="REMARK")
    private String remark;

	@Field(value="TAB_INDEX")
    private String tabIndex;

	@Field(value="PK_ADDREC")
    private String pkAddrec;

	@Field(value="PARENT_GRID_NAME")
    private String parentGridName;

	@Field(value="GROUP_INDEX")
    private String groupIndex;

    /** MODIFIER - 修改人 */
	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="GRID_COL_INDEX")
    private String gridColIndex;

	@Field(value="GRID_COL_TEXT")
    private String gridColText;

	@Field(value="MERGE_DISPLAY_FIELD")
    private String mergeDisplayField;

	@Field(value="COLUMN_TYPE2")
    private String columnType2;

	@Field(value="NDCO_TYPE")
    private String ndcoType;

	@Field(value="CONTRAST_FIELD")
    private String contrastField;


    public String getPkAddrecConfig(){
        return this.pkAddrecConfig;
    }
    public void setPkAddrecConfig(String pkAddrecConfig){
        this.pkAddrecConfig = pkAddrecConfig;
    }

    public String getGroupName(){
        return this.groupName;
    }
    public void setGroupName(String groupName){
        this.groupName = groupName;
    }

    public String getColumnText(){
        return this.columnText;
    }
    public void setColumnText(String columnText){
        this.columnText = columnText;
    }

    public String getUnit(){
        return this.unit;
    }
    public void setUnit(String unit){
        this.unit = unit;
    }

    public String getColumnType(){
        return this.columnType;
    }
    public void setColumnType(String columnType){
        this.columnType = columnType;
    }

    public String getFormat(){
        return this.format;
    }
    public void setFormat(String format){
        this.format = format;
    }

    public String getColumnName(){
        return this.columnName;
    }
    public void setColumnName(String columnName){
        this.columnName = columnName;
    }

    public String getDefaultValue(){
        return this.defaultValue;
    }
    public void setDefaultValue(String defaultValue){
        this.defaultValue = defaultValue;
    }

    public String getField(){
        return this.field;
    }
    public void setField(String field){
        this.field = field;
    }

    public String getFieldType(){
        return this.fieldType;
    }
    public void setFieldType(String fieldType){
        this.fieldType = fieldType;
    }

    public String getDtCode(){
        return this.dtCode;
    }
    public void setDtCode(String dtCode){
        this.dtCode = dtCode;
    }

    public String getVisible(){
        return this.visible;
    }
    public void setVisible(String visible){
        this.visible = visible;
    }

   /* public String getUpdate(){
        return this.update;
    }
    public void setUpdate(String update){
        this.update = update;
    }*/

    public String getAllownull(){
        return this.allownull;
    }
    public void setAllownull(String allownull){
        this.allownull = allownull;
    }

    public String getX(){
        return this.x;
    }
    public void setX(String x){
        this.x = x;
    }

    public String getY(){
        return this.y;
    }
    public void setY(String y){
        this.y = y;
    }

    public Double getWidth(){
        return this.width;
    }
    public void setWidth(Double width){
        this.width = width;
    }

    public Double getHeight(){
        return this.height;
    }
    public void setHeight(Double height){
        this.height = height;
    }

    public String getAttribute(){
        return this.attribute;
    }
    public void setAttribute(String attribute){
        this.attribute = attribute;
    }

    public String getEvent(){
        return this.event;
    }
    public void setEvent(String event){
        this.event = event;
    }

    public String getRemark(){
        return this.remark;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }

    public String getTabIndex(){
        return this.tabIndex;
    }
    public void setTabIndex(String tabIndex){
        this.tabIndex = tabIndex;
    }

    public String getPkAddrec(){
        return this.pkAddrec;
    }
    public void setPkAddrec(String pkAddrec){
        this.pkAddrec = pkAddrec;
    }

    public String getParentGridName(){
        return this.parentGridName;
    }
    public void setParentGridName(String parentGridName){
        this.parentGridName = parentGridName;
    }

    public String getGroupIndex(){
        return this.groupIndex;
    }
    public void setGroupIndex(String groupIndex){
        this.groupIndex = groupIndex;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public String getGridColIndex(){
        return this.gridColIndex;
    }
    public void setGridColIndex(String gridColIndex){
        this.gridColIndex = gridColIndex;
    }

    public String getGridColText(){
        return this.gridColText;
    }
    public void setGridColText(String gridColText){
        this.gridColText = gridColText;
    }

    public String getMergeDisplayField(){
        return this.mergeDisplayField;
    }
    public void setMergeDisplayField(String mergeDisplayField){
        this.mergeDisplayField = mergeDisplayField;
    }

    public String getColumnType2(){
        return this.columnType2;
    }
    public void setColumnType2(String columnType2){
        this.columnType2 = columnType2;
    }

    public String getNdcoType(){
        return this.ndcoType;
    }
    public void setNdcoType(String ndcoType){
        this.ndcoType = ndcoType;
    }

    public String getContrastField(){
        return this.contrastField;
    }
    public void setContrastField(String contrastField){
        this.contrastField = contrastField;
    }
}