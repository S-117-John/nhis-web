package com.zebone.nhis.common.module.emr.qc;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author 
 */
public class EmrGradeStandard{
    /**
     * 
     */
    private String pkStand;
    /**
     * 
     */
    private String pkOrg;
    /**
     * 
     */
    private String code;
    /**
     * 
     */
    private String name;
    /**
     * 
     */
    private String pyCode;
    /**
     * 
     */
    private String dCode;
    /**
     * 
     */
    private String shortName;
    /**
     * emr_grade_type.code

     */
    private String typeCode;
    /**
     *  save:-文档保存，commit-病案提交，check-质控检查 
     */
    private String euStandType;
    /**
     * 
     */
    private String docTypeCode;
    /**
     * 
     */
    private String scoreStand;
    /**
     * 扣分类型Std_Score_Type：single-单项，single_not-单否，each-每项，all_not-全否
     */
    private String scoreType;
    /**
     * 
     */
    private BigDecimal singleScore;
    /**
     * 
     */
    private String scoreXml;
    /**
     * 1:自动
0:手动


     */
    private String standMode;
    /**
     * 
     */
    private Integer sortNum;
    /**
     * 
     */
    private String pkDept;
    /**
     * 1:生效
0：未生效

     */
    private String euStatus;
    
    private String flagTime;
    
    
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
    
    private EmrGradeType gradeType;

    /**
     * 
     */
    public String getPkStand(){
        return this.pkStand;
    }

    /**
     * 
     */
    public void setPkStand(String pkStand){
        this.pkStand = pkStand;
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
    public String getCode(){
        return this.code;
    }

    /**
     * 
     */
    public void setCode(String code){
        this.code = code;
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
    public String getPyCode(){
        return this.pyCode;
    }

    /**
     * 
     */
    public void setPyCode(String pyCode){
        this.pyCode = pyCode;
    }    
    /**
     * 
     */
    public String getdCode(){
        return this.dCode;
    }

    /**
     * 
     */
    public void setdCode(String dCode){
        this.dCode = dCode;
    }    
    /**
     * 
     */
    public String getShortName(){
        return this.shortName;
    }

    /**
     * 
     */
    public void setShortName(String shortName){
        this.shortName = shortName;
    }    
    /**
     * emr_grade_type.code

     */
    public String getTypeCode(){
        return this.typeCode;
    }

    /**
     * emr_grade_type.code

     */
    public void setTypeCode(String typeCode){
        this.typeCode = typeCode;
    }    
    /**
     *  before：预先提醒，save:-文档保存，commit-病案提交，check-质控检查
     */
    public String getEuStandType(){
        return this.euStandType;
    }

    /**
     *  before：预先提醒，save:-文档保存，commit-病案提交，check-质控检查
     */
    public void setEuStandType(String euStandType){
        this.euStandType = euStandType;
    }    
    /**
     * 
     */
    public String getDocTypeCode(){
        return this.docTypeCode;
    }

    /**
     * 
     */
    public void setDocTypeCode(String docTypeCode){
        this.docTypeCode = docTypeCode;
    }    
    /**
     * 
     */
    public String getScoreStand(){
        return this.scoreStand;
    }

    /**
     * 
     */
    public void setScoreStand(String scoreStand){
        this.scoreStand = scoreStand;
    }    
    /**
     * 扣分类型Std_Score_Type：single-单项，single_not-单否，each-每项，all_not-全否
     */
    public String getScoreType(){
        return this.scoreType;
    }

    /**
     * 扣分类型Std_Score_Type：single-单项，single_not-单否，each-每项，all_not-全否
     */
    public void setScoreType(String scoreType){
        this.scoreType = scoreType;
    }    
    /**
     * 
     */
    public BigDecimal getSingleScore(){
        return this.singleScore;
    }

    /**
     * 
     */
    public void setSingleScore(BigDecimal singleScore){
        this.singleScore = singleScore;
    }    
    /**
     * 
     */
    public String getScoreXml(){
        return this.scoreXml;
    }

    /**
     * 
     */
    public void setScoreXml(String scoreXml){
        this.scoreXml = scoreXml;
    }    
    /**
     * 1:自动
0:手动


     */
    public String getStandMode(){
        return this.standMode;
    }

    /**
     * 1:自动
0:手动


     */
    public void setStandMode(String standMode){
        this.standMode = standMode;
    }    
    /**
     * 
     */
    public Integer getSortNum(){
        return this.sortNum;
    }

    /**
     * 
     */
    public void setSortNum(Integer sortNum){
        this.sortNum = sortNum;
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
     * 1:生效
0：未生效

     */
    public String getEuStatus(){
        return this.euStatus;
    }

    /**
     * 1:生效
0：未生效

     */
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
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

	public EmrGradeType getGradeType() {
		return gradeType;
	}

	public void setGradeType(EmrGradeType gradeType) {
		this.gradeType = gradeType;
	}

	public String getFlagTime() {
		return flagTime;
	}

	public void setFlagTime(String flagTime) {
		this.flagTime = flagTime;
	}    
    
}