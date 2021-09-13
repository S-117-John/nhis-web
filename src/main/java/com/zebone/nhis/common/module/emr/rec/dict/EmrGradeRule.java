package com.zebone.nhis.common.module.emr.rec.dict;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: EMR_GRADE_RULE 
 *
 * @since 2016-10-25 05:01:23
 */
@Table(value="EMR_GRADE_RULE")
public class EmrGradeRule   {

	@PK
	@Field(value="PK_RULE",id=KeyId.UUID)
    private String pkRule;
	
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

	@Field(value="SHORT_NAME")
    private String shortName;

    /** DT_RULE_TYPE - 01:(CONTENT内容)，02:(TIME时间)，03:(LOGIC逻辑) 04:(RELY依赖) 05:(CONFLICT:矛盾) */
	@Field(value="DT_RULE_TYPE")
    private String dtRuleType;

	@Field(value="DOC_TYPE_CODE")
    private String docTypeCode;

	@Field(value="RULE_XML")
    private String ruleXml;

	@Field(value="SORT_NUM")
    private Long sortNum;

	@Field(value="PK_DEPT")
    private String pkDept;

    /** EU_STATUS - 0:未生效
1：生效 */
	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="FLAG_TIME")
    private String flagTime;
	
	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(value="REMARK")
    private String remark;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(date=FieldType.ALL)
    private Date ts;

	private String status;

    public String getPkRule(){
        return this.pkRule;
    }
    public void setPkRule(String pkRule){
        this.pkRule = pkRule;
    }

    public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
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

    public String getShortName(){
        return this.shortName;
    }
    public void setShortName(String shortName){
        this.shortName = shortName;
    }

    public String getDtRuleType(){
        return this.dtRuleType;
    }
    public void setDtRuleType(String dtRuleType){
        this.dtRuleType = dtRuleType;
    }

    public String getDocTypeCode(){
        return this.docTypeCode;
    }
    public void setDocTypeCode(String docTypeCode){
        this.docTypeCode = docTypeCode;
    }

    public String getRuleXml(){
        return this.ruleXml;
    }
    public void setRuleXml(String ruleXml){
        this.ruleXml = ruleXml;
    }

    public Long getSortNum(){
        return this.sortNum;
    }
    public void setSortNum(Long sortNum){
        this.sortNum = sortNum;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }

    public String getRemark(){
        return this.remark;
    }
    public void setRemark(String remark){
        this.remark = remark;
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
    
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFlagTime() {
		return flagTime;
	}
	public void setFlagTime(String flagTime) {
		this.flagTime = flagTime;
	}
    
}
