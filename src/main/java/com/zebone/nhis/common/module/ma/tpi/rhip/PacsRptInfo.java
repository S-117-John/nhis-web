package com.zebone.nhis.common.module.ma.tpi.rhip;

import java.util.Date;

/**
 *
 * @author 
 */
public class PacsRptInfo{
    /**
     * 
     */
    private String sendedHospital;
    /**
     * 
     */
    private String recordNo;
    /**
     * 
     */
    private String hospitalCardid;
    /**
     * 
     */
    private String bodyOfCase;
    /**
     * 
     */
    private String clinicHospitalno;
    /**
     * 
     */
    private Integer deptType;
    /**
     * 
     */
    private String hisUid;
    /**
     * 
     */
    private Date reportDate;
    /**
     * 
     */
    private String reportDescTxt;
    /**
     * 
     */
    private String reportDiagTxt;
    /**
     * 
     */
    private String reportDocname;
    /**
     * 
     */
    private String positiveContext;

    private Date reqDate;
    
    /**
     * 
     */
    public String getSendedHospital(){
        return this.sendedHospital;
    }

    /**
     * 
     */
    public void setSendedHospital(String sendedHospital){
        this.sendedHospital = sendedHospital;
    }    
    /**
     * 
     */
    public String getRecordNo(){
        return this.recordNo;
    }

    /**
     * 
     */
    public void setRecordNo(String recordNo){
        this.recordNo = recordNo;
    }    
    /**
     * 
     */
    public String getHospitalCardid(){
        return this.hospitalCardid;
    }

    /**
     * 
     */
    public void setHospitalCardid(String hospitalCardid){
        this.hospitalCardid = hospitalCardid;
    }    
    /**
     * 
     */
    public String getBodyOfCase(){
        return this.bodyOfCase;
    }

    /**
     * 
     */
    public void setBodyOfCase(String bodyOfCase){
        this.bodyOfCase = bodyOfCase;
    }    
    /**
     * 
     */
    public String getClinicHospitalno(){
        return this.clinicHospitalno;
    }

    /**
     * 
     */
    public void setClinicHospitalno(String clinicHospitalno){
        this.clinicHospitalno = clinicHospitalno;
    }    
    /**
     * 
     */
    public Integer getDeptType(){
        return this.deptType;
    }

    /**
     * 
     */
    public void setDeptType(Integer deptType){
        this.deptType = deptType;
    }    
    /**
     * 
     */
    public String getHisUid(){
        return this.hisUid;
    }

    /**
     * 
     */
    public void setHisUid(String hisUid){
        this.hisUid = hisUid;
    }    
    /**
     * 
     */
    public Date getReportDate(){
        return this.reportDate;
    }

    /**
     * 
     */
    public void setReportDate(Date reportDate){
        this.reportDate = reportDate;
    }    
    /**
     * 
     */
    public String getReportDescTxt(){
        return this.reportDescTxt;
    }

    /**
     * 
     */
    public void setReportDescTxt(String reportDescTxt){
        this.reportDescTxt = reportDescTxt;
    }    
    /**
     * 
     */
    public String getReportDiagTxt(){
        return this.reportDiagTxt;
    }

    /**
     * 
     */
    public void setReportDiagTxt(String reportDiagTxt){
        this.reportDiagTxt = reportDiagTxt;
    }    
    /**
     * 
     */
    public String getReportDocname(){
        return this.reportDocname;
    }

    /**
     * 
     */
    public void setReportDocname(String reportDocname){
        this.reportDocname = reportDocname;
    }    
    
    
    public Date getReqDate() {
		return reqDate;
	}

	public void setReqDate(Date reqDate) {
		this.reqDate = reqDate;
	}

	/**
     * 
     */
    public String getPositiveContext(){
        return this.positiveContext;
    }

    /**
     * 
     */
    public void setPositiveContext(String positiveContext){
        this.positiveContext = positiveContext;
    }    
}