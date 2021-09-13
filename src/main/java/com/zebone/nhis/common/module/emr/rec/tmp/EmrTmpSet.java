package com.zebone.nhis.common.module.emr.rec.tmp;

import java.util.Date;

/**
 *
 * @author 
 */
public class EmrTmpSet{
    /**
     * 
     */
    private String pkSet;
    /**
     * 
     */
    private String pkOrg;
    /**
     * 
     */
    private String pkTmp;
    /**
     * association_rules：关联规则
get_data_set：取数据设置
calc_fields:计算字段设置

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
    private String pkEmp;
    /**
     * 
     */
    private String setXml;
    /**
     * 
     */
    private String remark;
    /**
     * 
     */
    private String delFlag;
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

    
    private String status;
    
    /**
     * 
     */
    public String getPkSet(){
        return this.pkSet;
    }

    /**
     * 
     */
    public void setPkSet(String pkSet){
        this.pkSet = pkSet;
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
    public String getPkTmp(){
        return this.pkTmp;
    }

    /**
     * 
     */
    public void setPkTmp(String pkTmp){
        this.pkTmp = pkTmp;
    }    
    /**
     * association_rules：关联规则
get_data_set：取数据设置
calc_fields:计算字段设置

     */
    public String getCode(){
        return this.code;
    }

    /**
     * association_rules：关联规则
get_data_set：取数据设置
calc_fields:计算字段设置

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
    public String getPkEmp(){
        return this.pkEmp;
    }

    /**
     * 
     */
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }    
    /**
     * 
     */
    public String getSetXml(){
        return this.setXml;
    }

    /**
     * 
     */
    public void setSetXml(String setXml){
        this.setXml = setXml;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}   
    
    
}