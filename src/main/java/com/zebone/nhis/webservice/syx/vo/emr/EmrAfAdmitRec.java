package com.zebone.nhis.webservice.syx.vo.emr;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 *
 *入院记录数据信息
 * @author 
 */
public class EmrAfAdmitRec{
   
    /**
     * 
     */
    @JsonProperty("hospital_id")
    private String medOrgCode;
    /**
     * 
     */
    @JsonProperty("hospital_name")
    private String medOrgName;

    @JsonProperty("inpatient_id")
    private String patNo;
    
    @JsonProperty("admission_num")
    private Integer times;
  
    @JsonProperty("inpatient_serial_num")
    private String codePv;
    
    @JsonProperty("name")
    private String name;

    @JsonProperty("sex")
    private String dtSex;
 
    @JsonProperty("age")
    private String ageTxt;
    
    @JsonFormat(pattern="yyyy-MM-dd")
    @JsonProperty("birthday")
    private Date birthDate;
    
    @JsonProperty("birthplace")
    private String birthAddr;

    @JsonProperty("native_place")
    private String originAddr;
    
    @JsonProperty("marital_status")
    private String marryCode;

    @JsonProperty("work_unit_addr")
    private String workUnit;
    
    @JsonProperty("mobile")
    private String mobile;

    @JsonProperty("profession")
    private String occupCode;
  
    @JsonIgnore
    private String occupName;
    
    
    @JsonProperty("present_addr")
    private String currAddr;
    
    /**
     * 
     */
    @JsonProperty("home_telephone")
    private String telNo;

    /**
     * 
     */
    @JsonProperty("contact_name")
    private String contactName;
    /**
     * 
     */
    @JsonProperty("relationship")
    private String contactRelatCode;


    @JsonProperty("contact_addr")
    private String contactAddr;
    /**
     * 
     */
    @JsonProperty("contact_mobile")
    private String contactPhone;
    
    @JsonProperty("admission_date")
    private Date admitTime;


    @JsonIgnore
    private Date recDate;
    
    @JsonIgnore
    private String docXml;
    
    //病史陈述者
    @JsonProperty("representor")
    private String medHistRep;
    //病史陈述者与患者关系
    @JsonProperty("representor_relationship")
    private String medHistRepRelate;

    
    //主诉
    @JsonProperty("chief_complaint")
    private String chiefComplaint;
    
    //现病史
    @JsonProperty("present_illness_history")
    private String presentIllnessHistory;
    //既往史
    @JsonProperty("past_medical_history")
    private String pastHistory;
    //个人史
    @JsonProperty("personal_history")
    private String personalHistory;
    //婚育史
    @JsonProperty("marriage_history")
    private String marriageHistory;
 	//月经史
    @JsonProperty("menstrual_history")
    private String menstrualHistory;
    //家族史
    @JsonProperty("family_history")
    private String familyHistory;
    //体格检查
    @JsonProperty("physical_examination")
    private String physicalExamination;
    //辅助检查
    @JsonProperty("auxiliary_examination")
    private String auxiliaryExamination;
    //病史小结
    @JsonProperty("medical_history_summary")
    private String medicalHistorySummary;
    //初步诊断
    @JsonProperty("primary_diagnosis")
    private String primary_diagnosis;
    
	/**
     * 
     */
	@JsonIgnore
    private String delFlag;
    /**
     * 
     */
	@JsonIgnore
    private String remark;
    /**
     * 
     */
	@JsonIgnore
    private String creator;
    /**
     * 
     */
	@JsonIgnore
    private Date createTime;
    /**
     * 
     */
	@JsonIgnore
    private Date ts;
	@JsonIgnore
    private String status;
	@JsonIgnore
    private String  pageType;
    
	@JsonIgnore
    public String codePi;
    
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonIgnore
    private String pkAdmitWard;

	
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
   
    /**
     * 
     */
    public String getMedOrgCode(){
        return this.medOrgCode;
    }

    /**
     * 
     */
    public void setMedOrgCode(String medOrgCode){
        this.medOrgCode = medOrgCode;
    }    
    /**
     * 
     */
    public String getMedOrgName(){
        return this.medOrgName;
    }

    /**
     * 
     */
    public void setMedOrgName(String medOrgName){
        this.medOrgName = medOrgName;
    }    


    public String getPatNo(){
        return this.patNo;
    }

    /**
     * 
     */
    public void setPatNo(String patNo){
        this.patNo = patNo;
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
    public String getDtSex(){
        return this.dtSex;
    }

    /**
     * 
     */
    public void setDtSex(String dtSex){
        this.dtSex = dtSex;
    }    
    /**
     * 
     */
    public Date getBirthDate(){
        return this.birthDate;
    }

    /**
     * 
     */
    public void setBirthDate(Date birthDate){
        this.birthDate = birthDate;
    }    

    /**
     * 
     */
    public String getAgeTxt(){
        return this.ageTxt;
    }

    /**
     * 
     */
    public void setAgeTxt(String ageTxt){
        this.ageTxt = ageTxt;
    }    
    /**
     * g
     */

	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

	public String getBirthAddr() {
		return birthAddr;
	}

	public void setBirthAddr(String birthAddr) {
		this.birthAddr = birthAddr;
	}

	public String getOriginAddr() {
		return originAddr;
	}

	public void setOriginAddr(String originAddr) {
		this.originAddr = originAddr;
	}

	public String getMarryCode() {
		return marryCode;
	}

	public void setMarryCode(String marryCode) {
		this.marryCode = marryCode;
	}

	public String getWorkUnit() {
		return workUnit;
	}

	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOccupCode() {
		return occupCode;
	}

	public void setOccupCode(String occupCode) {
		this.occupCode = occupCode;
	}

	public String getOccupName() {
		return occupName;
	}

	public void setOccupName(String occupName) {
		this.occupName = occupName;
	}

	public String getCurrAddr() {
		return currAddr;
	}

	public void setCurrAddr(String currAddr) {
		this.currAddr = currAddr;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactRelatCode() {
		return contactRelatCode;
	}

	public void setContactRelatCode(String contactRelatCode) {
		this.contactRelatCode = contactRelatCode;
	}

	public String getContactAddr() {
		return contactAddr;
	}

	public void setContactAddr(String contactAddr) {
		this.contactAddr = contactAddr;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public Date getAdmitTime() {
		return admitTime;
	}

	public void setAdmitTime(Date admitTime) {
		this.admitTime = admitTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	public String getPkAdmitWard() {
		return pkAdmitWard;
	}

	public void setPkAdmitWard(String pkAdmitWard) {
		this.pkAdmitWard = pkAdmitWard;
	}

	public String getMedHistRep() {
		return medHistRep;
	}

	public void setMedHistRep(String medHistRep) {
		this.medHistRep = medHistRep;
	}

	public String getMedHistRepRelate() {
		return medHistRepRelate;
	}

	public void setMedHistRepRelate(String medHistRepRelate) {
		this.medHistRepRelate = medHistRepRelate;
	}

	public Date getRecDate() {
		return recDate;
	}

	public void setRecDate(Date recDate) {
		this.recDate = recDate;
	}

	public String getDocXml() {
		return docXml;
	}

	public void setDocXml(String docXml) {
		this.docXml = docXml;
	}

	public String getChiefComplaint() {
		return chiefComplaint;
	}

	public void setChiefComplaint(String chiefComplaint) {
		this.chiefComplaint = chiefComplaint;
	}

	public String getPresentIllnessHistory() {
		return presentIllnessHistory;
	}

	public void setPresentIllnessHistory(String presentIllnessHistory) {
		this.presentIllnessHistory = presentIllnessHistory;
	}

	public String getPastHistory() {
		return pastHistory;
	}

	public void setPastHistory(String pastHistory) {
		this.pastHistory = pastHistory;
	}

	public String getPersonalHistory() {
		return personalHistory;
	}

	public void setPersonalHistory(String personalHistory) {
		this.personalHistory = personalHistory;
	}

	public String getMarriageHistory() {
		return marriageHistory;
	}

	public void setMarriageHistory(String marriageHistory) {
		this.marriageHistory = marriageHistory;
	}

	public String getMenstrualHistory() {
		return menstrualHistory;
	}

	public void setMenstrualHistory(String menstrualHistory) {
		this.menstrualHistory = menstrualHistory;
	}

	public String getFamilyHistory() {
		return familyHistory;
	}

	public void setFamilyHistory(String familyHistory) {
		this.familyHistory = familyHistory;
	}

	public String getPhysicalExamination() {
		return physicalExamination;
	}

	public void setPhysicalExamination(String physicalExamination) {
		this.physicalExamination = physicalExamination;
	}

	public String getAuxiliaryExamination() {
		return auxiliaryExamination;
	}

	public void setAuxiliaryExamination(String auxiliaryExamination) {
		this.auxiliaryExamination = auxiliaryExamination;
	}

	public String getMedicalHistorySummary() {
		return medicalHistorySummary;
	}

	public void setMedicalHistorySummary(String medicalHistorySummary) {
		this.medicalHistorySummary = medicalHistorySummary;
	}

	public String getPrimary_diagnosis() {
		return primary_diagnosis;
	}

	public void setPrimary_diagnosis(String primary_diagnosis) {
		this.primary_diagnosis = primary_diagnosis;
	}


	
}