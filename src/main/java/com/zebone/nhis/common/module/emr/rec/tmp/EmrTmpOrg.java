package com.zebone.nhis.common.module.emr.rec.tmp;

import java.util.Date;

/**
 *
 * @author 
 */
public class EmrTmpOrg{
    /**
     * 
     */
    private String pkTmporg;
    /**
     * 
     */
    private String pkTmp;
    /**
     * 
     */
    private String pkOrg;
    /**
     * 
     */
    private String pkDept;
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
    private String delFlag;
    /**
     * 
     */
    private Date ts;
    
    private String orgName;
    
    private String deptName;
    
    private String status;

    /**
     * 
     */
    public String getPkTmporg(){
        return this.pkTmporg;
    }

    /**
     * 
     */
    public void setPkTmporg(String pkTmporg){
        this.pkTmporg = pkTmporg;
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

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}    
    
}