package com.zebone.nhis.common.module.emr.qc;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author 
 */
public class EmrGradeRec{
    /**
     * 
     */
    private String pkGraderec;
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
    /**
     * link:环节质控
emp:医师自评
dept:科室质控
final:终末质控

     */
    private String euGradeType;
    /**
     * A：甲级
B：乙级
C：丙级

     */
    private String euGrade;
    /**
     * 
     */
    private BigDecimal score;
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
    private String pkEmpQc;
    /**
     * 
     */
    private Date qcDate;
    
    private String flagSend;
    
    private String flagResp;
    
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
    
    private List<EmrGradeItem> items;
    
    private String euUpdate;
    
    /**
     * 
     */
    public String getPkGraderec(){
        return this.pkGraderec;
    }

    /**
     * 
     */
    public void setPkGraderec(String pkGraderec){
        this.pkGraderec = pkGraderec;
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
     * link:环节质控
emp:医师自评
dept:科室质控
final:终末质控

     */
    public String getEuGradeType(){
        return this.euGradeType;
    }

    /**
     * link:环节质控
emp:医师自评
dept:科室质控
final:终末质控

     */
    public void setEuGradeType(String euGradeType){
        this.euGradeType = euGradeType;
    }    
    /**
     * A：甲级
B：乙级
C：丙级

     */
    public String getEuGrade(){
        return this.euGrade;
    }

    /**
     * A：甲级
B：乙级
C：丙级

     */
    public void setEuGrade(String euGrade){
        this.euGrade = euGrade;
    }    
    /**
     * 
     */
    public BigDecimal getScore(){
        return this.score;
    }

    /**
     * 
     */
    public void setScore(BigDecimal score){
        this.score = score;
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
    public String getPkEmpQc(){
        return this.pkEmpQc;
    }

    /**
     * 
     */
    public void setPkEmpQc(String pkEmpQc){
        this.pkEmpQc = pkEmpQc;
    }    
    /**
     * 
     */
    public Date getQcDate(){
        return this.qcDate;
    }

    /**
     * 
     */
    public void setQcDate(Date qcDate){
        this.qcDate = qcDate;
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

	public List<EmrGradeItem> getItems() {
		return items;
	}

	public void setItems(List<EmrGradeItem> items) {
		this.items = items;
	}

	public String getFlagSend() {
		return flagSend;
	}

	public void setFlagSend(String flagSend) {
		this.flagSend = flagSend;
	}

	public String getFlagResp() {
		return flagResp;
	}

	public void setFlagResp(String flagResp) {
		this.flagResp = flagResp;
	}

	public String getEuUpdate() {
		return euUpdate;
	}

	public void setEuUpdate(String euUpdate) {
		this.euUpdate = euUpdate;
	} 
    
    
}