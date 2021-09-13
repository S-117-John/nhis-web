package com.zebone.nhis.common.module.emr.scires;

import java.util.Date;
import java.util.List;

/**
 *
 * @author 
 */
public class EmrRptList{
    /**
     * 
     */
    private String pkRpt;
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
     * 0-管理，1-科研，2-公卫
     */
    private String euRptType;
    /**
     * 全院、科室、个人
     */
    private String euRptLevel;
    /**
     * 
     */
    private String resCode;
    /**
     * 
     */
    private String rptContent;
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
    private Integer sortNum;
    /**
     * 
     */
    private String pkDept;
    
    private String deptName;
    
    /**
     * 0-自定义，1-系统预置
     */
    private String flagSys;
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
    
    private String creatorName;
    /**
     * 
     */
    private Date createTime;
    /**
     * 
     */
    private Date ts;
    
    private String status;
    
    private List<EmrRptCnd> cndList;
    
    private List<EmrRptExp> expList;

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
     * 0-管理，1-科研，2-公卫
     */
    public String getEuRptType(){
        return this.euRptType;
    }

    /**
     * 0-管理，1-科研，2-公卫
     */
    public void setEuRptType(String euRptType){
        this.euRptType = euRptType;
    }    
    /**
     * 全院、科室、个人
     */
    public String getEuRptLevel(){
        return this.euRptLevel;
    }

    /**
     * 全院、科室、个人
     */
    public void setEuRptLevel(String euRptLevel){
        this.euRptLevel = euRptLevel;
    }    
    /**
     * 
     */
    public String getResCode(){
        return this.resCode;
    }

    /**
     * 
     */
    public void setResCode(String resCode){
        this.resCode = resCode;
    }    
    /**
     * 
     */
    public String getRptContent(){
        return this.rptContent;
    }

    /**
     * 
     */
    public void setRptContent(String rptContent){
        this.rptContent = rptContent;
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
     * 0-自定义，1-系统预置
     */
    public String getFlagSys(){
        return this.flagSys;
    }

    /**
     * 0-自定义，1-系统预置
     */
    public void setFlagSys(String flagSys){
        this.flagSys = flagSys;
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

	public List<EmrRptCnd> getCndList() {
		return cndList;
	}

	public void setCndList(List<EmrRptCnd> cndList) {
		this.cndList = cndList;
	}

	public List<EmrRptExp> getExpList() {
		return expList;
	}

	public void setExpList(List<EmrRptExp> expList) {
		this.expList = expList;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}   
    
    
}