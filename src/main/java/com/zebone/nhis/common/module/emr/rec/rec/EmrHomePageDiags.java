package com.zebone.nhis.common.module.emr.rec.rec;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

/**
 *
 * @author 
 */
@Table(value = "EMR_HOME_PAGE_DIAGS")
public class EmrHomePageDiags{
    /**
     * 
     */
    @PK
    @Field(value = "PK_PAGEDIAG", id = Field.KeyId.UUID)
    private String pkPagediag;
    /**
     * 
     */
    @Field(value = "PK_ORG")
    private String pkOrg;
    /**
     * 
     */
    @Field(value = "PK_PAGE")
    private String pkPage;
    /**
     * 
     */
    @Field(value = "SEQ_NO")
    private Integer seqNo;
    /**
     * 
     */
    @Field(value = "DT_DIAG_TYPE")
    private String dtDiagType;
    /**
     * 
     */
    @Field(value = "PK_DIAG")
    private String pkDiag;
    /**
     * 
     */
    @Field(value = "DIAG_CODE")
    private String diagCode;
    /**
     * 
     */
    @Field(value = "DIAG_NAME")
    private String diagName;
    /**
     * 
     */
    @Field(value = "DIAG_DESC")
    private String diagDesc;
    /**
     * 
     */
    @Field(value = "ADMIT_COND_CODE")
    private String admitCondCode;
    /**
     * 
     */
    @Field(value = "ADMIT_COND_NAME")
    private String admitCondName;
    /**
     * 
     */
    @Field(value = "FLAG_PRIMARY")
    private String flagPrimary;
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

    private String status;

    @Field(value = "CODE_DCDT")
    private String codeDcdt;

    @Field(value = "DESC_BODYPART")
    private String decsBodypart;

    @Field(value = "DESC_DRGPROP")
    private String descDrgprop;

    @Field(value = "FLAG_CURE")
    private String flagCure;


    public String getCodeDcdt() {
        return codeDcdt;
    }

    public void setCodeDcdt(String codeDcdt) {
        this.codeDcdt = codeDcdt;
    }

    public String getDecsBodypart() {
        return decsBodypart;
    }

    public void setDecsBodypart(String decsBodypart) {
        this.decsBodypart = decsBodypart;
    }

    public String getDescDrgprop() {
        return descDrgprop;
    }

    public void setDescDrgprop(String descDrgprop) {
        this.descDrgprop = descDrgprop;
    }

    public String getFlagCure() {
        return flagCure;
    }

    public void setFlagCure(String flagCure) {
        this.flagCure = flagCure;
    }

    /**
     * 
     */
    private String outComeCode;
    /**
     * 
     */
    private String outComeName;

    
    /**
     * 
     */
    public String getPkPagediag(){
        return this.pkPagediag;
    }

    /**
     * 
     */
    public void setPkPagediag(String pkPagediag){
        this.pkPagediag = pkPagediag;
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
    public String getDtDiagType(){
        return this.dtDiagType;
    }

    /**
     * 
     */
    public void setDtDiagType(String dtDiagType){
        this.dtDiagType = dtDiagType;
    }    
    /**
     * 
     */
    public String getPkDiag(){
        return this.pkDiag;
    }

    /**
     * 
     */
    public void setPkDiag(String pkDiag){
        this.pkDiag = pkDiag;
    }    
    /**
     * 
     */
    public String getDiagCode(){
        return this.diagCode;
    }

    /**
     * 
     */
    public void setDiagCode(String diagCode){
        this.diagCode = diagCode;
    }    
    /**
     * 
     */
    public String getDiagName(){
        return this.diagName;
    }

    /**
     * 
     */
    public void setDiagName(String diagName){
        this.diagName = diagName;
    }    
    /**
     * 
     */
    public String getDiagDesc(){
        return this.diagDesc;
    }

    /**
     * 
     */
    public void setDiagDesc(String diagDesc){
        this.diagDesc = diagDesc;
    }    
    /**
     * 
     */
    public String getAdmitCondCode(){
        return this.admitCondCode;
    }

    /**
     * 
     */
    public void setAdmitCondCode(String admitCondCode){
        this.admitCondCode = admitCondCode;
    }    
    /**
     * 
     */
    public String getAdmitCondName(){
        return this.admitCondName;
    }

    /**
     * 
     */
    public void setAdmitCondName(String admitCondName){
        this.admitCondName = admitCondName;
    }    
    /**
     * 
     */
    public String getFlagPrimary(){
        return this.flagPrimary;
    }

    /**
     * 
     */
    public void setFlagPrimary(String flagPrimary){
        this.flagPrimary = flagPrimary;
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
    
    /**
     * 
     */
    public String getOutcomeCode(){
        return this.outComeCode;
    }

    /**
     * 
     */
    public void setOutcomeCode(String outComeCode){
        this.outComeCode = outComeCode;
    }    
    /**
     * 
     */
    public String getOutcomeName(){
        return this.outComeName;
    }

    /**
     * 
     */
    public void setOutcomeName(String outComeName){
        this.outComeName = outComeName;
    }    

}