package com.zebone.nhis.common.module.emr.rec.rec;

import java.util.Date;

/**
 *
 * @author 
 */
public class EmrLisResult{
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
    private String pkCnord;
    
    private Date reqDate;
    
    /**
     * 
     */
    private Date testDate;
    /**
     * 
     */
    private String testName;
    /**
     * 
     */
    private String reqNo;
    /**
     * 
     */
    private String itemCode;
    /**
     * 
     */
    private String itemName;
    /**
     * 
     */
    private String result;
    /**
     * 
     */
    private String unit;
    /**
     * 
     */
    private String upperLimit;
    /**
     * 
     */
    private String lowerLimit;
    /**
     * 
     */
    private String range;
    /**
     * 
     */
    private String mark;
    
    private String smpNo;
    
    private String state;
    
    private String testCode;
    
    private String patMzZy;
    
    

	public String getPatMzZy() {
		return patMzZy;
	}

	public void setPatMzZy(String patMzZy) {
		this.patMzZy = patMzZy;
	}

	public String getTestCode() {
		return testCode;
	}

	public void setTestCode(String testCode) {
		this.testCode = testCode;
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
    public String getPkCnord(){
        return this.pkCnord;
    }

    /**
     * 
     */
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }    
    /**
     * 
     */
    public Date getTestDate(){
        return this.testDate;
    }

    /**
     * 
     */
    public void setTestDate(Date testDate){
        this.testDate = testDate;
    }    
    /**
     * 
     */
    public String getTestName(){
        return this.testName;
    }

    /**
     * 
     */
    public void setTestName(String testName){
        this.testName = testName;
    }    
    /**
     * 
     */
    public String getReqNo(){
        return this.reqNo;
    }

    /**
     * 
     */
    public void setReqNo(String reqNo){
        this.reqNo = reqNo;
    }    
    /**
     * 
     */
    public String getItemCode(){
        return this.itemCode;
    }

    /**
     * 
     */
    public void setItemCode(String itemCode){
        this.itemCode = itemCode;
    }    
    /**
     * 
     */
    public String getItemName(){
        return this.itemName;
    }

    /**
     * 
     */
    public void setItemName(String itemName){
        this.itemName = itemName;
    }    
    /**
     * 
     */
    public String getResult(){
        return this.result;
    }

    /**
     * 
     */
    public void setResult(String result){
        this.result = result;
    }    
    /**
     * 
     */
    public String getUnit(){
        return this.unit;
    }

    /**
     * 
     */
    public void setUnit(String unit){
        this.unit = unit;
    }    
    /**
     * 
     */
    public String getUpperLimit(){
        return this.upperLimit;
    }

    /**
     * 
     */
    public void setUpperLimit(String upperLimit){
        this.upperLimit = upperLimit;
    }    
    /**
     * 
     */
    public String getLowerLimit(){
        return this.lowerLimit;
    }

    /**
     * 
     */
    public void setLowerLimit(String lowerLimit){
        this.lowerLimit = lowerLimit;
    }    
    /**
     * 
     */
    public String getRange(){
        return this.range;
    }

    /**
     * 
     */
    public void setRange(String range){
        this.range = range;
    }    
    /**
     * 
     */
    public String getMark(){
        return this.mark;
    }

    /**
     * 
     */
    public void setMark(String mark){
        this.mark = mark;
    }

	public String getSmpNo() {
		return smpNo;
	}

	public void setSmpNo(String smpNo) {
		this.smpNo = smpNo;
	}

	public Date getReqDate() {
		return reqDate;
	}

	public void setReqDate(Date reqDate) {
		this.reqDate = reqDate;
	}    
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}