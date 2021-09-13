package com.zebone.nhis.common.module.emr.scires;

import java.util.Date;
import java.util.List;

/**
 *
 * @author 
 */
public class EmrRptCnd{
    /**
     * 
     */
    private String pkCnd;
    /**
     * 
     */
    private String pkOrg;
    /**
     * 
     */
    private String pkRpt;
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
    private String euRttype;
    /**
     * 
     */
    private String euCndtype;
    /**
     * 
     */
    private String objCode;
    /**
     * 
     */
    private String objName;
    /**
     * 
     */
    private String logic;
    /**
     * 
     */
    private String valueCode;
    /**
     * 
     */
    private String valueName;
    /**
     * 
     */
    private Integer euGrade;
    /**
     * 
     */
    private String pkCndUp;
    /**
     * 
     */
    private String fullPath;
    /**
     * "0":元素
 "1":段落
 "2":组合
     */
    private String euObjType;
    /**
     * "0":基本信息
 "1":文档
     */
    private String euObjClass;
    /**
     * 1 日期 ，0 数字
     */
    private String euDataType;
    /**
     * 
     */
    private Integer sortNum;
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
    
    private String status;

    private List<EmrRptCnd> childs;
    
    private boolean match;
    
    /**
     * 
     */
    public String getPkCnd(){
        return this.pkCnd;
    }

    /**
     * 
     */
    public void setPkCnd(String pkCnd){
        this.pkCnd = pkCnd;
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
    public String getPkRpt(){
        return this.pkRpt;
    }

    /**
     * 
     */
    public void setPkRpt(String pkRpt){
        this.pkRpt = pkRpt;
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
    public String getEuRttype(){
        return this.euRttype;
    }

    /**
     * 
     */
    public void setEuRttype(String euRttype){
        this.euRttype = euRttype;
    }    
    /**
     * 
     */
    public String getEuCndtype(){
        return this.euCndtype;
    }

    /**
     * 
     */
    public void setEuCndtype(String euCndtype){
        this.euCndtype = euCndtype;
    }    
    /**
     * 
     */
    public String getObjCode(){
        return this.objCode;
    }

    /**
     * 
     */
    public void setObjCode(String objCode){
        this.objCode = objCode;
    }    
    /**
     * 
     */
    public String getObjName(){
        return this.objName;
    }

    /**
     * 
     */
    public void setObjName(String objName){
        this.objName = objName;
    }    
    /**
     * 
     */
    public String getLogic(){
        return this.logic;
    }

    /**
     * 
     */
    public void setLogic(String logic){
        this.logic = logic;
    }    
    /**
     * 
     */
    public String getValueCode(){
        return this.valueCode;
    }

    /**
     * 
     */
    public void setValueCode(String valueCode){
        this.valueCode = valueCode;
    }    
    /**
     * 
     */
    public String getValueName(){
        return this.valueName;
    }

    /**
     * 
     */
    public void setValueName(String valueName){
        this.valueName = valueName;
    }    
    /**
     * 
     */
    public Integer getEuGrade(){
        return this.euGrade;
    }

    /**
     * 
     */
    public void setEuGrade(Integer euGrade){
        this.euGrade = euGrade;
    }    
    /**
     * 
     */
    public String getPkCndUp(){
        return this.pkCndUp;
    }

    /**
     * 
     */
    public void setPkCndUp(String pkCndUp){
        this.pkCndUp = pkCndUp;
    }    
    /**
     * 
     */
    public String getFullPath(){
        return this.fullPath;
    }

    /**
     * 
     */
    public void setFullPath(String fullPath){
        this.fullPath = fullPath;
    }    
    /**
     * "0":元素
 "1":段落
 "2":组合
     */
    public String getEuObjType(){
        return this.euObjType;
    }

    /**
     * "0":元素
 "1":段落
 "2":组合
     */
    public void setEuObjType(String euObjType){
        this.euObjType = euObjType;
    }    
    /**
     * "0":基本信息
 "1":文档
     */
    public String getEuObjClass(){
        return this.euObjClass;
    }

    /**
     * "0":基本信息
 "1":文档
     */
    public void setEuObjClass(String euObjClass){
        this.euObjClass = euObjClass;
    }    
    /**
     * 1 日期 ，0 数字
     */
    public String getEuDataType(){
        return this.euDataType;
    }

    /**
     * 1 日期 ，0 数字
     */
    public void setEuDataType(String euDataType){
        this.euDataType = euDataType;
    }    
    /**
     * 
     */
    public Integer getSortNum(){
        return this.sortNum;
    }

    /**
     * 
     */
    public void setSortNum(Integer sortNum){
        this.sortNum = sortNum;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<EmrRptCnd> getChilds() {
		return childs;
	}

	public void setChilds(List<EmrRptCnd> childs) {
		this.childs = childs;
	}

	public boolean isMatch() {
		return match;
	}

	public void setMatch(boolean match) {
		this.match = match;
	}  
    
	
}