package com.zebone.nhis.common.module.emr.rec.rec;

import java.util.Date;

/**
 *
 * @author 
 */
public class EmrPatDiags{
    /**
     * 
     */
    private String pkPatdiag;
    /**
     * 
     */
    private String pkOrg;
    /**
     * 
     */
    private String pkPv;
    /**
     * 
     */
    private String pkDiag;
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
    private String dtDiagType;
    /**
     * 
     */
    private Integer seqNum;
    /**
     * 
     */
    private Date diagDate;
    /**
     * 
     */
    private Integer sortNum;
    /**
     * 
     */
    private Integer pkParent;
    /**
     * 
     */
    private String flagTcm;
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
    
    /**
     * 
     */
    public String getPkPatdiag(){
        return this.pkPatdiag;
    }

    /**
     * 
     */
    public void setPkPatdiag(String pkPatdiag){
        this.pkPatdiag = pkPatdiag;
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
    public String getPkPv(){
        return this.pkPv;
    }

    /**
     * 
     */
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }    
    /**
     * 
     */
    public String getPkDiag(){
        return this.pkDiag;
    }

    /**
     * 
     */
    public void setPkDiag(String pkDiag){
        this.pkDiag = pkDiag;
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
    public String getDtDiagType(){
        return this.dtDiagType;
    }

    /**
     * 
     */
    public void setDtDiagType(String dtDiagType){
        this.dtDiagType = dtDiagType;
    }    
    /**
     * 
     */
    public Integer getSeqNum(){
        return this.seqNum;
    }

    /**
     * 
     */
    public void setSeqNum(Integer seqNum){
        this.seqNum = seqNum;
    }    
    /**
     * 
     */
    public Date getDiagDate(){
        return this.diagDate;
    }

    /**
     * 
     */
    public void setDiagDate(Date diagDate){
        this.diagDate = diagDate;
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
    public Integer getPkParent(){
        return this.pkParent;
    }

    /**
     * 
     */
    public void setPkParent(Integer pkParent){
        this.pkParent = pkParent;
    }    
    /**
     * 
     */
    public String getFlagTcm(){
        return this.flagTcm;
    }

    /**
     * 
     */
    public void setFlagTcm(String flagTcm){
        this.flagTcm = flagTcm;
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
    
}