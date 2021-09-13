package com.zebone.nhis.common.module.emr.mgr;

import java.util.Date;
import java.util.List;

/**
 *
 * @author 
 */
public class EmrBorrowRec{
    /**
     * 
     */
    private String pkBorrow;
    /**
     * 
     */
    private String pkOrg;
    
    private String pkPv;
    /**
     * 
     */
    private String pkPi;
    /**
     * 
     */
    private Integer times;
    /**
     * 
     */
    private String pkEmpApply;
    /**
     * 
     */
    private Date applyDate;
    /**
     * 天
     */
    private Integer timeLimit;
    /**
     * 
     */
    private Date beginDate;
    /**
     * 
     */
    private Date endDate;
    /**
     * 
     */
    private String applyTxt;
    /**
     * 0申请
1批准
2拒绝

     */
    private String euStatus;
    /**
     * 
     */
    private String pkEmpApprove;
    /**
     * 
     */
    private Date approveDate;
    /**
     * 
     */
    private Date returnDate;
    /**
     * 
     */
    private String pkEmpReceive;
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
    
    private List<EmrBorrowDoc> docs;

    private String name;
    
    private String ageTxt;
    
    private String sexName;
    
    private String patNo;
    
    private String empApplyName;
    
    private String empApproveName;
    
    private String empReceiveName;
    
    
    /**
     * 
     */
    public String getPkBorrow(){
        return this.pkBorrow;
    }

    /**
     * 
     */
    public void setPkBorrow(String pkBorrow){
        this.pkBorrow = pkBorrow;
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
    public String getPkPi(){
        return this.pkPi;
    }

    /**
     * 
     */
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }    
    /**
     * 
     */
    public Integer getTimes(){
        return this.times;
    }

    /**
     * 
     */
    public void setTimes(Integer times){
        this.times = times;
    }    
    /**
     * 
     */
    public String getPkEmpApply(){
        return this.pkEmpApply;
    }

    /**
     * 
     */
    public void setPkEmpApply(String pkEmpApply){
        this.pkEmpApply = pkEmpApply;
    }    
    /**
     * 
     */
    public Date getApplyDate(){
        return this.applyDate;
    }

    /**
     * 
     */
    public void setApplyDate(Date applyDate){
        this.applyDate = applyDate;
    }    
    /**
     * 天
     */
    public Integer getTimeLimit(){
        return this.timeLimit;
    }

    /**
     * 天
     */
    public void setTimeLimit(Integer timeLimit){
        this.timeLimit = timeLimit;
    }    
    /**
     * 
     */
    public Date getBeginDate(){
        return this.beginDate;
    }

    /**
     * 
     */
    public void setBeginDate(Date beginDate){
        this.beginDate = beginDate;
    }    
    /**
     * 
     */
    public Date getEndDate(){
        return this.endDate;
    }

    /**
     * 
     */
    public void setEndDate(Date endDate){
        this.endDate = endDate;
    }    
    /**
     * 
     */
    public String getApplyTxt(){
        return this.applyTxt;
    }

    /**
     * 
     */
    public void setApplyTxt(String applyTxt){
        this.applyTxt = applyTxt;
    }    
    /**
     * 0申请
1批准
2拒绝

     */
    public String getEuStatus(){
        return this.euStatus;
    }

    /**
     * 0申请
1批准
2拒绝

     */
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }    
    /**
     * 
     */
    public String getPkEmpApprove(){
        return this.pkEmpApprove;
    }

    /**
     * 
     */
    public void setPkEmpApprove(String pkEmpApprove){
        this.pkEmpApprove = pkEmpApprove;
    }    
    /**
     * 
     */
    public Date getApproveDate(){
        return this.approveDate;
    }

    /**
     * 
     */
    public void setApproveDate(Date approveDate){
        this.approveDate = approveDate;
    }    
    /**
     * 
     */
    public Date getReturnDate(){
        return this.returnDate;
    }

    /**
     * 
     */
    public void setReturnDate(Date returnDate){
        this.returnDate = returnDate;
    }    
    /**
     * 
     */
    public String getPkEmpReceive(){
        return this.pkEmpReceive;
    }

    /**
     * 
     */
    public void setPkEmpReceive(String pkEmpReceive){
        this.pkEmpReceive = pkEmpReceive;
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

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public List<EmrBorrowDoc> getDocs() {
		return docs;
	}

	public void setDocs(List<EmrBorrowDoc> docs) {
		this.docs = docs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAgeTxt() {
		return ageTxt;
	}

	public void setAgeTxt(String ageTxt) {
		this.ageTxt = ageTxt;
	}

	public String getSexName() {
		return sexName;
	}

	public void setSexName(String sexName) {
		this.sexName = sexName;
	}

	public String getPatNo() {
		return patNo;
	}

	public void setPatNo(String patNo) {
		this.patNo = patNo;
	}

	public String getEmpApplyName() {
		return empApplyName;
	}

	public void setEmpApplyName(String empApplyName) {
		this.empApplyName = empApplyName;
	}

	public String getEmpApproveName() {
		return empApproveName;
	}

	public void setEmpApproveName(String empApproveName) {
		this.empApproveName = empApproveName;
	}

	public String getEmpReceiveName() {
		return empReceiveName;
	}

	public void setEmpReceiveName(String empReceiveName) {
		this.empReceiveName = empReceiveName;
	}    
    
	
    
}