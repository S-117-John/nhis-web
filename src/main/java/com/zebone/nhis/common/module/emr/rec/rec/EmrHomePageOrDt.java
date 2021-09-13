package com.zebone.nhis.common.module.emr.rec.rec;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author 
 */
@Table(value = "EMR_HOME_PAGE_OR_DT")
public class EmrHomePageOrDt{
    /**
     * 
     */
    @PK
    @Field(value = "PK_ORDT", id = Field.KeyId.UUID)
    private String pkOrdt;
    /**
     * 
     */
    @Field(value = "PK_ORG")
    private String pkOrg;
    /**
     * 
     */
    @Field(value = "PK_PAGEOR")
    private String pkPageor;
    /**
     * 
     */
    @Field(value = "SEQ_NO")
    private Integer seqNo;
    /**
     * 
     */
    @Field(value = "BEGIN_DATE")
    private Date beginDate;
    /**
     * 
     */
    @Field(value = "END_DATE")
    private Date endDate;
    /**
     * 
     */
    @Field(value = "DRUG_NAME")
    private String drugName;
    /**
     * 
     */
    @Field(value = "COURSE_CODE")
    private String courseCode;
    /**
     * 
     */
    @Field(value = "COURSE_NAME")
    private String courseName;
    /**
     * 
     */
    @Field(value = "EFFECT_CODE")
    private String effectCode;
    /**
     * 
     */
    @Field(value = "EFFECT_NAME")
    private String effectName;
    /**
     * 
     */
    @Field(value = "DEL_FLAG")
    private String delFlag;
    /**
     * 
     */
    @Field(value = "REMARK")
    private String remark;
    /**
     * 
     */
    @Field(userfield = "pkEmp", userfieldscop = Field.FieldType.INSERT)
    private String creator;
    /**
     * 
     */
    @Field(value = "create_time", date = Field.FieldType.INSERT)
    private Date createTime;
    /**
     * 
     */
    @Field(value = "TS")
    private Date ts;

    private String Status;
    
    /**
     * 
     */
    public String getPkOrdt(){
        return this.pkOrdt;
    }

    /**
     * 
     */
    public void setPkOrdt(String pkOrdt){
        this.pkOrdt = pkOrdt;
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
    public String getPkPageor(){
        return this.pkPageor;
    }

    /**
     * 
     */
    public void setPkPageor(String pkPageor){
        this.pkPageor = pkPageor;
    }    
    /**
     * 
     */
    public Integer getSeqNo(){
        return this.seqNo;
    }

    /**
     * 
     */
    public void setSeqNo(Integer seqNo){
        this.seqNo = seqNo;
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
    public String getDrugName(){
        return this.drugName;
    }

    /**
     * 
     */
    public void setDrugName(String drugName){
        this.drugName = drugName;
    }    
    /**
     * 
     */
    public String getCourseCode(){
        return this.courseCode;
    }

    /**
     * 
     */
    public void setCourseCode(String courseCode){
        this.courseCode = courseCode;
    }    
    /**
     * 
     */
    public String getCourseName(){
        return this.courseName;
    }

    /**
     * 
     */
    public void setCourseName(String courseName){
        this.courseName = courseName;
    }    
  
    /**
     * 
     */
    public String getEffectCode(){
        return this.effectCode;
    }

    /**
     * 
     */
    public void setEffectCode(String effectCode){
        this.effectCode = effectCode;
    }    
    /**
     * 
     */
    public String getEffectName(){
        return this.effectName;
    }

    /**
     * 
     */
    public void setEffectName(String effectName){
        this.effectName = effectName;
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
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}  
    
    
}