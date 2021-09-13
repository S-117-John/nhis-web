package com.zebone.nhis.common.module.emr.qc;

import java.util.Date;

/**
 *
 * @author 
 */
public class EmrMedRecTask{
    /**
     * 
     */
    private String pkTask;
    /**
     * 
     */
    private String pkOrg;
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
    private String pkPv;
    
    private String euTasktype;
    
    /**
     * 
     */
    private String ruleCode;
    /**
     * 
     */
    private String pkEvtrec;
    /**
     * 
     */
    private String typeCode;
    
    private String pkRec;
    
    /**
     * 0:非周期
1：周期
     */
    private String flagRecCycle;
    /**
     * 
     */
    private String taskDesc;
    /**
     * 0：未完成
1：完成

     */
    private String euStatus;
    /**
     * 0:未完成
1：正常完成
2：延迟完成
3：部分完成
4：提前完成

     */
    private String euRecStatus;
    
    private Date recDate;
    
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
    private Date finishDate;
    
    private Date beginDateRmd;
    
    /**
     * 0：不超时
1：超时

     */
    private String flagTimeout;
    /**
     * 单位：小时
     */
    private Integer remainTime;
    /**
     * 
     */
    private Integer dueNum;
    /**
     * 
     */
    private Integer factNum;
    /**
     * 
     */
    private String pkDept;
    /**
     * 
     */
    private String pkEmp;
    /**
     * 
     */
    private String flagShow;
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
    
    private Integer deductNum;
    
    private String typeName;
    /**
     * 
     */
    public String getPkTask(){
        return this.pkTask;
    }

    /**
     * 
     */
    public void setPkTask(String pkTask){
        this.pkTask = pkTask;
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
    public String getRuleCode(){
        return this.ruleCode;
    }

    /**
     * 
     */
    public void setRuleCode(String ruleCode){
        this.ruleCode = ruleCode;
    }    


    public String getPkEvtrec() {
		return pkEvtrec;
	}

	public void setPkEvtrec(String pkEvtrec) {
		this.pkEvtrec = pkEvtrec;
	}

	/**
     * 
     */
    public String getTypeCode(){
        return this.typeCode;
    }

    /**
     * 
     */
    public void setTypeCode(String typeCode){
        this.typeCode = typeCode;
    }    
    /**
     * 0:非周期
1：周期
     */
    public String getFlagRecCycle(){
        return this.flagRecCycle;
    }

    /**
     * 0:非周期
1：周期
     */
    public void setFlagRecCycle(String flagRecCycle){
        this.flagRecCycle = flagRecCycle;
    }    
    /**
     * 
     */
    public String getTaskDesc(){
        return this.taskDesc;
    }

    /**
     * 
     */
    public void setTaskDesc(String taskDesc){
        this.taskDesc = taskDesc;
    }    
    /**
     * 0：未完成
1：完成

     */
    public String getEuStatus(){
        return this.euStatus;
    }

    /**
     * 0：未完成
1：完成

     */
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }    
    /**
     * 0:未完成
1：正常完成
2：延迟完成
3：部分完成
4：提前完成

     */
    public String getEuRecStatus(){
        return this.euRecStatus;
    }

    /**
     * 0:未完成
1：正常完成
2：延迟完成
3：部分完成
4：提前完成

     */
    public void setEuRecStatus(String euRecStatus){
        this.euRecStatus = euRecStatus;
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
    public Date getFinishDate(){
        return this.finishDate;
    }

    /**
     * 
     */
    public void setFinishDate(Date finishDate){
        this.finishDate = finishDate;
    }    
    /**
     * 0：不超时
1：超时

     */
    public String getFlagTimeout(){
        return this.flagTimeout;
    }

    /**
     * 0：不超时
1：超时

     */
    public void setFlagTimeout(String flagTimeout){
        this.flagTimeout = flagTimeout;
    }    
    /**
     * 单位：小时
     */
    public Integer getRemainTime(){
        return this.remainTime;
    }

    /**
     * 单位：小时
     */
    public void setRemainTime(Integer remainTime){
        this.remainTime = remainTime;
    }    
    /**
     * 
     */
    public Integer getDueNum(){
        return this.dueNum;
    }

    /**
     * 
     */
    public void setDueNum(Integer dueNum){
        this.dueNum = dueNum;
    }    
    /**
     * 
     */
    public Integer getFactNum(){
        return this.factNum;
    }

    /**
     * 
     */
    public void setFactNum(Integer factNum){
        this.factNum = factNum;
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
    public String getFlagShow(){
        return this.flagShow;
    }

    /**
     * 
     */
    public void setFlagShow(String flagShow){
        this.flagShow = flagShow;
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

	public String getPkRec() {
		return pkRec;
	}

	public void setPkRec(String pkRec) {
		this.pkRec = pkRec;
	}

	public Date getRecDate() {
		return recDate;
	}

	public void setRecDate(Date recDate) {
		this.recDate = recDate;
	}

	public String getEuTasktype() {
		return euTasktype;
	}

	public void setEuTasktype(String euTasktype) {
		this.euTasktype = euTasktype;
	}

	public Date getBeginDateRmd() {
		return beginDateRmd;
	}

	public void setBeginDateRmd(Date beginDateRmd) {
		this.beginDateRmd = beginDateRmd;
	}

	public Integer getDeductNum() {
		return deductNum;
	}

	public void setDeductNum(Integer deductNum) {
		this.deductNum = deductNum;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}   
    
	
}