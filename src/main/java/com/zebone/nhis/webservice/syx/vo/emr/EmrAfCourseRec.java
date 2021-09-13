package com.zebone.nhis.webservice.syx.vo.emr;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 *
 *病程数据信息
 * @author 
 */
public class EmrAfCourseRec{
   
	@JsonIgnore
	private String pkPv;
	 
	@JsonIgnore
	private String pkRec;
	
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
    private String typeCode;
    
    @JsonIgnore
    private String flagCourse;
    @JsonIgnore
    private String flagFirst;
    @JsonIgnore
    private Date recDate;
    
    @JsonIgnore
    private String docXml;
    
    
    //首次病程
    @JsonProperty("first_disease_course")
    private String firstDiseaseCourse;
    
    //日常病程
    @JsonProperty("daily_disease_course")
    private List<String> dailyDiseaseCourse;
    
    //手术记录
    @JsonProperty("operation_record")
    private List<String> operationRecord;
    
    //抢救记录
    @JsonProperty("salvage_record")
    private List<String> salvageRecord;
    
    //出院记录
    @JsonProperty("discharge_record")
    private String dischargeRecord;
    
    //出院小结
    @JsonProperty("discharge_abstract")
    private String dischargeAbstract;
    
    //死亡
    @JsonProperty("death_record")
    private String deathRecord;
    
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
    
	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getPkRec() {
		return pkRec;
	}

	public void setPkRec(String pkRec) {
		this.pkRec = pkRec;
	}

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

	public String getFirstDiseaseCourse() {
		return firstDiseaseCourse;
	}

	public void setFirstDiseaseCourse(String firstDiseaseCourse) {
		this.firstDiseaseCourse = firstDiseaseCourse;
	}

	public String getFlagFirst() {
		return flagFirst;
	}

	public void setFlagFirst(String flagFirst) {
		this.flagFirst = flagFirst;
	}

	public String getFlagCourse() {
		return flagCourse;
	}

	public void setFlagCourse(String flagCourse) {
		this.flagCourse = flagCourse;
	}

	public List<String> getDailyDiseaseCourse() {
		return dailyDiseaseCourse;
	}

	public void setDailyDiseaseCourse(List<String> dailyDiseaseCourse) {
		this.dailyDiseaseCourse = dailyDiseaseCourse;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public List<String> getOperationRecord() {
		return operationRecord;
	}

	public void setOperationRecord(List<String> operationRecord) {
		this.operationRecord = operationRecord;
	}

	public List<String> getSalvageRecord() {
		return salvageRecord;
	}

	public void setSalvageRecord(List<String> salvageRecord) {
		this.salvageRecord = salvageRecord;
	}

	public String getDischargeRecord() {
		return dischargeRecord;
	}

	public void setDischargeRecord(String dischargeRecord) {
		this.dischargeRecord = dischargeRecord;
	}

	public String getDischargeAbstract() {
		return dischargeAbstract;
	}

	public void setDischargeAbstract(String dischargeAbstract) {
		this.dischargeAbstract = dischargeAbstract;
	}

	public String getDeathRecord() {
		return deathRecord;
	}

	public void setDeathRecord(String deathRecord) {
		this.deathRecord = deathRecord;
	}


}