package com.zebone.nhis.common.module.emr.rec.rec;

import java.util.Date;

/**
 *
 * @author 
 */
public class EmrRisResult{
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
    /**
     * 
     */
    private Date checkDate;
    /**
     * 
     */
    private String checkName;
    /**
     * 
     */
    private String reqNo;
    /**
     * 
     */
    private String checkResult;
    /**
     * 
     */
    private String checkDiags;
    
    private String dateCheck;

    private String reportUrl;
    
    private String imageUrl;
    
    //超声，用来判断检查结果是否超声
    private String superSound;
    //住院号
    private String clinicHospitalno;
    //门诊号
    private String hospitalCardid;
    
    
    public String getClinicHospitalno() {
		return clinicHospitalno;
	}

	public void setClinicHospitalno(String clinicHospitalno) {
		this.clinicHospitalno = clinicHospitalno;
	}

	public String getHospitalCardid() {
		return hospitalCardid;
	}

	public void setHospitalCardid(String hospitalCardid) {
		this.hospitalCardid = hospitalCardid;
	}

	public String getSuperSound() {
		return superSound;
	}

	public void setSuperSound(String superSound) {
		this.superSound = superSound;
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
    public Date getCheckDate(){
        return this.checkDate;
    }

    /**
     * 
     */
    public void setCheckDate(Date checkDate){
        this.checkDate = checkDate;
    }    
    /**
     * 
     */
    public String getCheckName(){
        return this.checkName;
    }

    /**
     * 
     */
    public void setCheckName(String checkName){
        this.checkName = checkName;
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
    public String getCheckResult(){
        return this.checkResult;
    }

    /**
     * 
     */
    public void setCheckResult(String checkResult){
        this.checkResult = checkResult;
    }    
    /**
     * 
     */
    public String getCheckDiags(){
        return this.checkDiags;
    }

    /**
     * 
     */
    public void setCheckDiags(String checkDiags){
        this.checkDiags = checkDiags;
    }

	public String getDateCheck() {
		return dateCheck;
	}

	public void setDateCheck(String dateCheck) {
		this.dateCheck = dateCheck;
	}

	public String getReportUrl() {
		return reportUrl;
	}

	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}    
    
}