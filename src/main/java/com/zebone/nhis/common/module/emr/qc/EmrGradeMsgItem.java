package com.zebone.nhis.common.module.emr.qc;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author 
 */
public class EmrGradeMsgItem{
    /**
     * 
     */
    private String pkGradeMsgitem;
    /**
     * 
     */
    private String pkOrg;
    /**
     * 病历评分消息记录
     */
    private String pkGradeMsgrec;
    
    private String pkGradeitem;
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
    private String score;
    /**
     * 
     */
    private String pkDept;
    /**
     * 
     */
    private String pkEmpQc;
    /**
     * 
     */
    private Date qcDate;
    /**
     * 
     */
    private String pkEmp;
    /**
     * 
     */
    private String pkEmpSend;
    /**
     * 
     */
    private Date sendDate;
    /**
     * 
     */
    private String pkEmpReceive;
    /**
     * 
     */
    private Date receiveDate;
    /**
     * 
     */
    private String pkEmpResponse;
    /**
     * 
     */
    private Date responseDate;
    /**
     * 
     */
    private String responseTxt;
    
    private String flagSubmit;
    
    private Date submitDate;
    
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
    private String euStatus;
    public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

	private String status;
    
    /**
     * 
     */
    public String getPkGradeMsgitem(){
        return this.pkGradeMsgitem;
    }

    /**
     * 
     */
    public void setPkGradeMsgitem(String pkGradeMsgitem){
        this.pkGradeMsgitem = pkGradeMsgitem;
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
     * 病历评分消息记录
     */
    public String getPkGradeMsgrec(){
        return this.pkGradeMsgrec;
    }

    /**
     * 病历评分消息记录
     */
    public void setPkGradeMsgrec(String pkGradeMsgrec){
        this.pkGradeMsgrec = pkGradeMsgrec;
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
    public String getPkEmpSend(){
        return this.pkEmpSend;
    }

    /**
     * 
     */
    public void setPkEmpSend(String pkEmpSend){
        this.pkEmpSend = pkEmpSend;
    }    
    /**
     * 
     */
    public Date getSendDate(){
        return this.sendDate;
    }

    /**
     * 
     */
    public void setSendDate(Date sendDate){
        this.sendDate = sendDate;
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
    public Date getReceiveDate(){
        return this.receiveDate;
    }

    /**
     * 
     */
    public void setReceiveDate(Date receiveDate){
        this.receiveDate = receiveDate;
    }    
    /**
     * 
     */
    public String getPkEmpResponse(){
        return this.pkEmpResponse;
    }

    /**
     * 
     */
    public void setPkEmpResponse(String pkEmpResponse){
        this.pkEmpResponse = pkEmpResponse;
    }    
    /**
     * 
     */
    public Date getResponseDate(){
        return this.responseDate;
    }

    /**
     * 
     */
    public void setResponseDate(Date responseDate){
        this.responseDate = responseDate;
    }    
    /**
     * 
     */
    public String getResponseTxt(){
        return this.responseTxt;
    }

    /**
     * 
     */
    public void setResponseTxt(String responseTxt){
        this.responseTxt = responseTxt;
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

	public String getPkGradeitem() {
		return pkGradeitem;
	}

	public void setPkGradeitem(String pkGradeitem) {
		this.pkGradeitem = pkGradeitem;
	}

	public String getFlagSubmit() {
		return flagSubmit;
	}

	public void setFlagSubmit(String flagSubmit) {
		this.flagSubmit = flagSubmit;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}    
    
    
}