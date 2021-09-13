package com.zebone.nhis.common.module.emr.rec.rec;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

/**
 *
 * @author 
 */
@Table(value = "EMR_HOME_PAGE_OPS")
public class EmrHomePageOps{
    /**
     * 
     */
    @PK
    @Field(value = "PK_OPS", id = Field.KeyId.UUID)
    private String pkOps;
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
    @Field(value = "OP_CODE")
    private String opCode;
    /**
     * 
     */
    @Field(value = "OP_NAME")
    private String opName;
    /**
     * 
     */
    @Field(value = "OP_DATE")
    private Date opDate;
    /**
     * 
     */
    @Field(value = "GRADE_CODE")
    private String gradeCode;
    /**
     * 
     */
    @Field(value = "GRADE_NAME")
    private String gradeName;
    /**
     * 
     */
    @Field(value = "PK_EMP_OP")
    private String pkEmpOp;
    /**
     * 
     */
    @Field(value = "OP_DOC_NAME")
    private String opDocName;
    /**
     * 
     */
    @Field(value = "PK_EMP_OPI")
    private String pkEmpOpi;
    /**
     * 
     */
    @Field(value = "OPI_NAME")
    private String opiName;
    /**
     * 
     */
    @Field(value = "PK_EMP_OPII")
    private String pkEmpOpii;
    /**
     * 
     */
    @Field(value = "OPII_NAME")
    private String opiiName;
    /**
     * 
     */
    @Field(value = "INCISION_TYPE_CODE")
    private String incisionTypeCode;
    /**
     * 
     */
    @Field(value = "INCISION_TYPE_NAME")
    private String incisionTypeName;
    /**
     * 
     */
    @Field(value = "HEAL_GRADE_CODE")
    private String healGradeCode;
    /**
     * 
     */
    @Field(value = "HEAL_GRADE_NAME")
    private String healGradeName;
    /**
     * 
     */
    @Field(value = "ANES_TYPE_CODE")
    private String anesTypeCode;
    /**
     * 
     */
    @Field(value = "ANES_TYPE_NAME")
    private String anesTypeName;
    /**
     * 
     */
    @Field(value = "PK_EMP_ANES")
    private String pkEmpAnes;
    /**
     * 
     */
    @Field(value = "ANES_DOC_NAME")
    private String anesDocName;

    @Field(value = "FLAG_ELECTIVE")
    private String flagElective;
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

    @Field(value = "EU_TYPE")
    private String euType;

    @Field(value = "NNIS_CODE")
    private String nnisCode;

    @Field(value = "NNIS_NAME")
    private String nnisName;

    @Field(value = "ASA_CODE")
    private String asaCode;

    @Field(value = "ASA_NAME")
    private String asaName;
    
    @Field(userfield = "EMP_CODE_OP",userfieldscop=FieldType.NONE)
    private String empCodeOp;
    
    @Field(userfield = "EMP_CODE_OPI",userfieldscop=FieldType.NONE)
    private String empCodeOpi;
    
    @Field(userfield = "EMP_CODE_OPII",userfieldscop=FieldType.NONE)
    private String empCodeOpii;
    
    @Field(userfield = "EMP_CODE_ANES",userfieldscop=FieldType.NONE)
	private String empCodeAnes;
   
    
    /**
     * 
     */
    public String getPkOps(){
        return this.pkOps;
    }

    /**
     * 
     */
    public void setPkOps(String pkOps){
        this.pkOps = pkOps;
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
    public String getOpCode(){
        return this.opCode;
    }

    /**
     * 
     */
    public void setOpCode(String opCode){
        this.opCode = opCode;
    }    
    /**
     * 
     */
    public String getOpName(){
        return this.opName;
    }

    /**
     * 
     */
    public void setOpName(String opName){
        this.opName = opName;
    }    
    /**
     * 
     */
    public Date getOpDate(){
        return this.opDate;
    }

    /**
     * 
     */
    public void setOpDate(Date opDate){
        this.opDate = opDate;
    }    
    /**
     * 
     */
    public String getGradeCode(){
        return this.gradeCode;
    }

    /**
     * 
     */
    public void setGradeCode(String gradeCode){
        this.gradeCode = gradeCode;
    }    
    /**
     * 
     */
    public String getGradeName(){
        return this.gradeName;
    }

    /**
     * 
     */
    public void setGradeName(String gradeName){
        this.gradeName = gradeName;
    }    
    /**
     * 
     */
    public String getPkEmpOp(){
        return this.pkEmpOp;
    }

    /**
     * 
     */
    public void setPkEmpOp(String pkEmpOp){
        this.pkEmpOp = pkEmpOp;
    }    
    /**
     * 
     */
    public String getOpDocName(){
        return this.opDocName;
    }

    /**
     * 
     */
    public void setOpDocName(String opDocName){
        this.opDocName = opDocName;
    }    
    /**
     * 
     */
    public String getPkEmpOpi(){
        return this.pkEmpOpi;
    }

    /**
     * 
     */
    public void setPkEmpOpi(String pkEmpOpi){
        this.pkEmpOpi = pkEmpOpi;
    }    
    /**
     * 
     */
    public String getOpiName(){
        return this.opiName;
    }

    /**
     * 
     */
    public void setOpiName(String opiName){
        this.opiName = opiName;
    }    
    /**
     * 
     */
    public String getPkEmpOpii(){
        return this.pkEmpOpii;
    }

    /**
     * 
     */
    public void setPkEmpOpii(String pkEmpOpii){
        this.pkEmpOpii = pkEmpOpii;
    }    
    /**
     * 
     */
    public String getOpiiName(){
        return this.opiiName;
    }

    /**
     * 
     */
    public void setOpiiName(String opiiName){
        this.opiiName = opiiName;
    }    
    /**
     * 
     */
    public String getIncisionTypeCode(){
        return this.incisionTypeCode;
    }

    /**
     * 
     */
    public void setIncisionTypeCode(String incisionTypeCode){
        this.incisionTypeCode = incisionTypeCode;
    }    
    /**
     * 
     */
    public String getIncisionTypeName(){
        return this.incisionTypeName;
    }

    /**
     * 
     */
    public void setIncisionTypeName(String incisionTypeName){
        this.incisionTypeName = incisionTypeName;
    }    
    /**
     * 
     */
    public String getHealGradeCode(){
        return this.healGradeCode;
    }

    /**
     * 
     */
    public void setHealGradeCode(String healGradeCode){
        this.healGradeCode = healGradeCode;
    }    
    /**
     * 
     */
    public String getHealGradeName(){
        return this.healGradeName;
    }

    /**
     * 
     */
    public void setHealGradeName(String healGradeName){
        this.healGradeName = healGradeName;
    }    
    /**
     * 
     */
    public String getAnesTypeCode(){
        return this.anesTypeCode;
    }

    /**
     * 
     */
    public void setAnesTypeCode(String anesTypeCode){
        this.anesTypeCode = anesTypeCode;
    }    
    /**
     * 
     */
    public String getAnesTypeName(){
        return this.anesTypeName;
    }

    /**
     * 
     */
    public void setAnesTypeName(String anesTypeName){
        this.anesTypeName = anesTypeName;
    }    
    /**
     * 
     */
    public String getPkEmpAnes(){
        return this.pkEmpAnes;
    }

    /**
     * 
     */
    public void setPkEmpAnes(String pkEmpAnes){
        this.pkEmpAnes = pkEmpAnes;
    }    
    /**
     * 
     */
    public String getAnesDocName(){
        return this.anesDocName;
    }

    /**
     * 
     */
    public void setAnesDocName(String anesDocName){
        this.anesDocName = anesDocName;
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

	public String getFlagElective() {
		return flagElective;
	}

	public void setFlagElective(String flagElective) {
		this.flagElective = flagElective;
	}

	public String getEuType() {
		return euType;
	}

	public void setEuType(String euType) {
		this.euType = euType;
	}

	public String getNnisCode() {
		return nnisCode;
	}

	public void setNnisCode(String nnisCode) {
		this.nnisCode = nnisCode;
	}

	public String getNnisName() {
		return nnisName;
	}

	public void setNnisName(String nnisName) {
		this.nnisName = nnisName;
	}

	public String getAsaCode() {
		return asaCode;
	}

	public void setAsaCode(String asaCode) {
		this.asaCode = asaCode;
	}

	public String getAsaName() {
		return asaName;
	}

	public void setAsaName(String asaName) {
		this.asaName = asaName;
	}

	public String getEmpCodeOp() {
		return empCodeOp;
	}

	public void setEmpCodeOp(String empCodeOp) {
		this.empCodeOp = empCodeOp;
	}

	public String getEmpCodeAnes() {
		return empCodeAnes;
	}

	public void setEmpCodeAnes(String empCodeAnes) {
		this.empCodeAnes = empCodeAnes;
	}

	public String getEmpCodeOpi() {
		return empCodeOpi;
	}

	public void setEmpCodeOpi(String empCodeOpi) {
		this.empCodeOpi = empCodeOpi;
	}

	public String getEmpCodeOpii() {
		return empCodeOpii;
	}

	public void setEmpCodeOpii(String empCodeOpii) {
		this.empCodeOpii = empCodeOpii;
	}

	
}