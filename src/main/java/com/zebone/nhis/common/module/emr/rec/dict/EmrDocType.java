package com.zebone.nhis.common.module.emr.rec.dict;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EMR_DOC_TYPE 
 *
 * @since 2016-09-26 01:49:45
 */
@Table(value="EMR_DOC_TYPE")
public class EmrDocType extends BaseModule  {

	@PK
	@Field(value="PK_DOCTYPE",id=KeyId.UUID)
    private String pkDoctype;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="PARENT_TYPE")
    private String parentType;

	@Field(value="LEVELS")
    private Integer levels;

	@Field(value="PY_CODE")
    private String pyCode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="WS_CODE")
    private String wsCode;

    /** EU_VISIT - 1、门诊
2、住院 */
	@Field(value="EU_VISIT")
    private String euVisit;

	@Field(value="EU_USED")
    private String euUsed;

	@Field(value="SORT_NUM")
    private Integer sortNum;

	@Field(value="TITLE")
    private String title;
	
	@Field(value="FLAG_COURSE")
    private String flagCourse;
	
	@Field(value="FLAG_FIRST")
    private String flagFirst;
	
	@Field(value="FLAG_NEW_PAGE")
    private String flagNewPage;
	
	@Field(value="FLAG_HOME_PAGE")
    private String flagHomePage;
    
	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_WARD")
    private String pkWard;

	@Field(value="RESERVE")
    private String reserve;
	
	@Field(value="REMARK")
    private String remark;

	private String codeStatus;
	
    public String getPkDoctype(){
        return this.pkDoctype;
    }
    public void setPkDoctype(String pkDoctype){
        this.pkDoctype = pkDoctype;
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

    public String getParentType(){
        return this.parentType;
    }
    public void setParentType(String parentType){
        this.parentType = parentType;
    }

    public Integer getLevels(){
        return this.levels;
    }
    public void setLevels(Integer levels){
        this.levels = levels;
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

    public String getEuVisit(){
        return this.euVisit;
    }
    public void setEuVisit(String euVisit){
        this.euVisit = euVisit;
    }

    public String getEuUsed(){
        return this.euUsed;
    }
    public void setEuUsed(String euUsed){
        this.euUsed = euUsed;
    }

    public Integer getSortNum(){
        return this.sortNum;
    }
    public void setSortNum(Integer sortNum){
        this.sortNum = sortNum;
    }

    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkWard(){
        return this.pkWard;
    }
    public void setPkWard(String pkWard){
        this.pkWard = pkWard;
    }

    public String getRemark(){
        return this.remark;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }
	public String getCodeStatus() {
		return codeStatus;
	}
	public void setCodeStatus(String codeStatus) {
		this.codeStatus = codeStatus;
	}
	public String getFlagCourse() {
		return flagCourse;
	}
	public void setFlagCourse(String flagCourse) {
		this.flagCourse = flagCourse;
	}
	public String getFlagNewPage() {
		return flagNewPage;
	}
	public void setFlagNewPage(String flagNewPage) {
		this.flagNewPage = flagNewPage;
	}
	public String getFlagFirst() {
		return flagFirst;
	}
	public void setFlagFirst(String flagFirst) {
		this.flagFirst = flagFirst;
	}
	public String getFlagHomePage() {
		return flagHomePage;
	}
	public void setFlagHomePage(String flagHomePage) {
		this.flagHomePage = flagHomePage;
	}
	public String getReserve() {
		return reserve;
	}
	public void setReserve(String reserve) {
		this.reserve = reserve;
	}
    
}