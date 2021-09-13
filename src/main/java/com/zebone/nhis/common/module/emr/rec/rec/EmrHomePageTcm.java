package com.zebone.nhis.common.module.emr.rec.rec;

import java.util.Date;

/**
 *
 * @author 
 */
public class EmrHomePageTcm{
    /**
     * 
     */
    private String pkPage;
    /**
     * 
     */
    private String pkOrg;
    /**
     * 治疗类别 □ 1.中医（ 1.1 中医   1.2民族医）    2.中西医     3.西医
     */
    private String therTypeCode;
    /**
     * 
     */
    private String therTypeName;
    /**
     * □ 1. 中医  2. 西医  3 否
     */
    private String cpCode;
    /**
     * 
     */
    private String cpName;
    /**
     * 
     */
    private String diagCodeClinicTcm;
    /**
     * 
     */
    private String diagNameClinicTcm;
    /**
     * 
     */
    private String flagPharTcm;
    /**
     * 
     */
    private String flagEquipTcm;
    /**
     * 
     */
    private String flagTechTcm;
    /**
     * 
     */
    private String flagNursingTcm;
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
    public String getPkPage(){
        return this.pkPage;
    }

    /**
     * 
     */
    public void setPkPage(String pkPage){
        this.pkPage = pkPage;
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
     * 治疗类别 □ 1.中医（ 1.1 中医   1.2民族医）    2.中西医     3.西医
     */
    public String getTherTypeCode(){
        return this.therTypeCode;
    }

    /**
     * 治疗类别 □ 1.中医（ 1.1 中医   1.2民族医）    2.中西医     3.西医
     */
    public void setTherTypeCode(String therTypeCode){
        this.therTypeCode = therTypeCode;
    }    
    /**
     * 
     */
    public String getTherTypeName(){
        return this.therTypeName;
    }

    /**
     * 
     */
    public void setTherTypeName(String therTypeName){
        this.therTypeName = therTypeName;
    }    
    /**
     * □ 1. 中医  2. 西医  3 否
     */
    public String getCpCode(){
        return this.cpCode;
    }

    /**
     * □ 1. 中医  2. 西医  3 否
     */
    public void setCpCode(String cpCode){
        this.cpCode = cpCode;
    }    
    /**
     * 
     */
    public String getCpName(){
        return this.cpName;
    }

    /**
     * 
     */
    public void setCpName(String cpName){
        this.cpName = cpName;
    }    
    /**
     * 
     */
    public String getDiagCodeClinicTcm(){
        return this.diagCodeClinicTcm;
    }

    /**
     * 
     */
    public void setDiagCodeClinicTcm(String diagCodeClinicTcm){
        this.diagCodeClinicTcm = diagCodeClinicTcm;
    }    
    /**
     * 
     */
    public String getDiagNameClinicTcm(){
        return this.diagNameClinicTcm;
    }

    /**
     * 
     */
    public void setDiagNameClinicTcm(String diagNameClinicTcm){
        this.diagNameClinicTcm = diagNameClinicTcm;
    }    
    /**
     * 
     */
    public String getFlagPharTcm(){
        return this.flagPharTcm;
    }

    /**
     * 
     */
    public void setFlagPharTcm(String flagPharTcm){
        this.flagPharTcm = flagPharTcm;
    }    
    /**
     * 
     */
    public String getFlagEquipTcm(){
        return this.flagEquipTcm;
    }

    /**
     * 
     */
    public void setFlagEquipTcm(String flagEquipTcm){
        this.flagEquipTcm = flagEquipTcm;
    }    
    /**
     * 
     */
    public String getFlagTechTcm(){
        return this.flagTechTcm;
    }

    /**
     * 
     */
    public void setFlagTechTcm(String flagTechTcm){
        this.flagTechTcm = flagTechTcm;
    }    
    /**
     * 
     */
    public String getFlagNursingTcm(){
        return this.flagNursingTcm;
    }

    /**
     * 
     */
    public void setFlagNursingTcm(String flagNursingTcm){
        this.flagNursingTcm = flagNursingTcm;
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
}