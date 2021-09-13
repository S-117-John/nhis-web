package com.zebone.nhis.common.module.emr.qc;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author 
 */
public class EmrGradeItem{
    /**
     * 
     */
    private String pkGradeitem;
    /**
     * 
     */
    private String pkOrg;
    /**
     * 评分记录ID
     */
    private String pkGraderec;
    
    private String pkRec;
    
    /**
     * 
     */
    private String standCode;
    /**
     * 
     */
    private String typeCode;
    /**
     * 
     */
    private String scoreDesc;
    /**
     * 
     */
    private Short num;
    /**
     * 
     */
    private String score;
    /**
     * 
     */
    private String pkDept;
    /**
     * 
     */
    private String pkEmp;
    /**
     * 0：手工录入
1：系统产生

     */
    private String euScore;
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
    
    private String gradeName;
    private String gradeEmpName;
    
    private EmrGradeType gradeType;
    private EmrGradeRec gradeRec;
    private EmrGradeStandard std;
    private String flagAmend;
    
    private String euStatus;
    
    /**
     * 完成标志
     * 取值0~1，跟emr_med_rec_task表中eu_rec_status有关联
     */
    private String flagFinish;
    
    public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

	/**
     * 
     */
    public String getPkGradeitem(){
        return this.pkGradeitem;
    }

    /**
     * 
     */
    public void setPkGradeitem(String pkGradeitem){
        this.pkGradeitem = pkGradeitem;
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
     * 评分记录ID
     */
    public String getPkGraderec(){
        return this.pkGraderec;
    }

    /**
     * 评分记录ID
     */
    public void setPkGraderec(String pkGraderec){
        this.pkGraderec = pkGraderec;
    }    
    /**
     * 
     */
    public String getStandCode(){
        return this.standCode;
    }

    /**
     * 
     */
    public void setStandCode(String standCode){
        this.standCode = standCode;
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
     * 
     */
    public String getScoreDesc(){
        return this.scoreDesc;
    }

    /**
     * 
     */
    public void setScoreDesc(String scoreDesc){
        this.scoreDesc = scoreDesc;
    }    
    /**
     * 
     */
    public Short getNum(){
        return this.num;
    }

    /**
     * 
     */
    public void setNum(Short num){
        this.num = num;
    }    
    /**
     * 
     */
    public String getScore(){
        return this.score;
    }

    /**
     * 
     */
    public void setScore(String score){
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
     * 0：手工录入
1：系统产生

     */
    public String getEuScore(){
        return this.euScore;
    }

    /**
     * 0：手工录入
1：系统产生

     */
    public void setEuScore(String euScore){
        this.euScore = euScore;
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

	
	public EmrGradeType getGradeType() {
		return gradeType;
	}

	public void setGradeType(EmrGradeType gradeType) {
		this.gradeType = gradeType;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getGradeEmpName() {
		return gradeEmpName;
	}

	public void setGradeEmpName(String gradeEmpName) {
		this.gradeEmpName = gradeEmpName;
	}

	public EmrGradeRec getGradeRec() {
		return gradeRec;
	}

	public void setGradeRec(EmrGradeRec gradeRec) {
		this.gradeRec = gradeRec;
	}

	public EmrGradeStandard getStd() {
		return std;
	}

	public void setStd(EmrGradeStandard std) {
		this.std = std;
	}

	public String getFlagAmend() {
		return flagAmend;
	}

	public void setFlagAmend(String flagAmend) {
		this.flagAmend = flagAmend;
	}

	public String getPkRec() {
		return pkRec;
	}

	public void setPkRec(String pkRec) {
		this.pkRec = pkRec;
	}

	public String getFlagFinish() {
		return flagFinish;
	}

	public void setFlagFinish(String flagFinish) {
		this.flagFinish = flagFinish;
	}
	

    
}