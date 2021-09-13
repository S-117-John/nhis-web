package com.zebone.nhis.common.module.base.bd.code;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_REPORT_PARAM 
 *
 * @since 2018-07-21 12:55:14
 */
@Table(value="BD_REPORT_PARAM")
public class BdReportParam  extends BaseModule{

	@PK
	@Field(value="PK_RPTPARAM",id=KeyId.UUID)
    private String pkRptparam;

	@Field(value="PK_REPORT")
    private String pkReport;

	@Field(value="CODE")
    private String code;

	@Field(value="CODE_PARAM")
    private String codeParam;

	@Field(value="NAME_PARAM")
    private String nameParam;

	@Field(value="EU_TYPE")
    private String euType;

	@Field(value="DEFAULT_VAL")
    private String defaultVal;

	@Field(value="DATA_SOURCE")
    private String dataSource;

	@Field(value="GLOBAL_VAR")
    private String globalVar;

	@Field(value="SORT_NO")
    private Integer sortNo;
	
	@Field(value="MODIFY_TIME",date=FieldType.ALL)
    private Date modifyTime;

	@Field(value="CODE_DEFDOCLIST")
    private String codeDefdoclist;
	
	@Field(value="RANGE_ENUM") 
	private String rangeEnum;
	
	@Field(value="FLAG_MULSEL") 
	private String flagMulsel;

    public String getFlagMulsel() {
		return flagMulsel;
	}
	public void setFlagMulsel(String flagMulsel) {
		this.flagMulsel = flagMulsel;
	}
	public String getCodeDefdoclist() {
		return codeDefdoclist;
	}
	public void setCodeDefdoclist(String codeDefdoclist) {
		this.codeDefdoclist = codeDefdoclist;
	}
	public String getPkRptparam(){
        return this.pkRptparam;
    }
    public void setPkRptparam(String pkRptparam){
        this.pkRptparam = pkRptparam;
    }

    public String getPkReport(){
        return this.pkReport;
    }
    public void setPkReport(String pkReport){
        this.pkReport = pkReport;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getCodeParam(){
        return this.codeParam;
    }
    public void setCodeParam(String codeParam){
        this.codeParam = codeParam;
    }

    public String getNameParam(){
        return this.nameParam;
    }
    public void setNameParam(String nameParam){
        this.nameParam = nameParam;
    }

    public String getEuType(){
        return this.euType;
    }
    public void setEuType(String euType){
        this.euType = euType;
    }

    public String getDefaultVal(){
        return this.defaultVal;
    }
    public void setDefaultVal(String defaultVal){
        this.defaultVal = defaultVal;
    }

    public String getDataSource(){
        return this.dataSource;
    }
    public void setDataSource(String dataSource){
        this.dataSource = dataSource;
    }

    public String getGlobalVar(){
        return this.globalVar;
    }
    public void setGlobalVar(String globalVar){
        this.globalVar = globalVar;
    }

    public Integer getSortNo(){
        return this.sortNo;
    }
    public void setSortNo(Integer sortNo){
        this.sortNo = sortNo;
    }
   
    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }
	public String getRangeEnum() {
		return rangeEnum;
	}
	public void setRangeEnum(String rangeEnum) {
		this.rangeEnum = rangeEnum;
	}

}