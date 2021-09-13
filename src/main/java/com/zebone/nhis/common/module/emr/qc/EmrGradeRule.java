package com.zebone.nhis.common.module.emr.qc;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author 
 */
public class EmrGradeRule{
    /**
     * 
     */
    private String pkRule;
    /**
     * 
     */
    private String pkOrg;
    /**
     * 
     */
    private String code;
    /**
     * 
     */
    private String name;
    /**
     * 
     */
    private String pyCode;
    /**
     * 
     */
    private String dCode;
    /**
     * 
     */
    private String shortName;
    /**
     * 01:(content内容)，02:(time时间)，03:(logic逻辑) 04:(rely依赖) 05:(conflict:矛盾)
     */
    private String dtRuleType;
    /**
     * 
     */
    private String docTypeCode;
    /**
     * 
     */
    private String ruleXml;
    /**
     * 
     */
    private BigDecimal sortNum;
    /**
     * 
     */
    private String pkDept;
    /**
     * 0:未生效
1：生效

     */
    private String euStatus;
    
    private String flagTime;
    
    /**
     * 
     */
    private String delFlag;
    /**
     * 
     */
    private String remark;
    /**
     * 
     */
    private String creator;
    /**
     * 
     */
    private Date createTime;
    /**
     * 
     */
    private Date ts;

    /**
     * 
     */
    public String getPkRule(){
        return this.pkRule;
    }

    /**
     * 
     */
    public void setPkRule(String pkRule){
        this.pkRule = pkRule;
    }    
    /**
     * 
     */
    public String getPkOrg(){
        return this.pkOrg;
    }

    /**
     * 
     */
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }    
    /**
     * 
     */
    public String getCode(){
        return this.code;
    }

    /**
     * 
     */
    public void setCode(String code){
        this.code = code;
    }    
    /**
     * 
     */
    public String getName(){
        return this.name;
    }

    /**
     * 
     */
    public void setName(String name){
        this.name = name;
    }    
    /**
     * 
     */
    public String getPyCode(){
        return this.pyCode;
    }

    /**
     * 
     */
    public void setPyCode(String pyCode){
        this.pyCode = pyCode;
    }    
    /**
     * 
     */
    public String getdCode(){
        return this.dCode;
    }

    /**
     * 
     */
    public void setdCode(String dCode){
        this.dCode = dCode;
    }    
    /**
     * 
     */
    public String getShortName(){
        return this.shortName;
    }

    /**
     * 
     */
    public void setShortName(String shortName){
        this.shortName = shortName;
    }    
    /**
     * 01:(content内容)，02:(time时间)，03:(logic逻辑) 04:(rely依赖) 05:(conflict:矛盾)
     */
    public String getDtRuleType(){
        return this.dtRuleType;
    }

    /**
     * 01:(content内容)，02:(time时间)，03:(logic逻辑) 04:(rely依赖) 05:(conflict:矛盾)
     */
    public void setDtRuleType(String dtRuleType){
        this.dtRuleType = dtRuleType;
    }    
    /**
     * 
     */
    public String getDocTypeCode(){
        return this.docTypeCode;
    }

    /**
     * 
     */
    public void setDocTypeCode(String docTypeCode){
        this.docTypeCode = docTypeCode;
    }    
    /**
     * 
     */
    public String getRuleXml(){
        return this.ruleXml;
    }

    /**
     * 
     */
    public void setRuleXml(String ruleXml){
        this.ruleXml = ruleXml;
    }    
    /**
     * 
     */
    public BigDecimal getSortNum(){
        return this.sortNum;
    }

    /**
     * 
     */
    public void setSortNum(BigDecimal sortNum){
        this.sortNum = sortNum;
    }    
    /**
     * 
     */
    public String getPkDept(){
        return this.pkDept;
    }

    /**
     * 
     */
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }    
    /**
     * 0:未生效
1：生效

     */
    public String getEuStatus(){
        return this.euStatus;
    }

    /**
     * 0:未生效
1：生效

     */
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }    
    /**
     * 
     */
    public String getDelFlag(){
        return this.delFlag;
    }

    /**
     * 
     */
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }    
    /**
     * 
     */
    public String getRemark(){
        return this.remark;
    }

    /**
     * 
     */
    public void setRemark(String remark){
        this.remark = remark;
    }    
    /**
     * 
     */
    public String getCreator(){
        return this.creator;
    }

    /**
     * 
     */
    public void setCreator(String creator){
        this.creator = creator;
    }    
    /**
     * 
     */
    public Date getCreateTime(){
        return this.createTime;
    }

    /**
     * 
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }    
    /**
     * 
     */
    public Date getTs(){
        return this.ts;
    }

    /**
     * 
     */
    public void setTs(Date ts){
        this.ts = ts;
    }

	public String getFlagTime() {
		return flagTime;
	}

	public void setFlagTime(String flagTime) {
		this.flagTime = flagTime;
	}


    
}