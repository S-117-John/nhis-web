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
@Table(value = "EMR_HOME_PAGE_OR")
public class EmrHomePageOr{
    /**
     * 
     */
    @PK
    @Field(value = "PK_PAGEOR", id = Field.KeyId.UUID)
    private String pkPageor;
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
     * 1：P病理
2：C临床
     */
    @Field(value = "STAGE_TYPE_CODE")
    private String stageTypeCode;
    /**
     * 
     */
    @Field(value = "STAGE_TYPE_NAME")
    private String stageTypeName;
    /**
     * 0/1/2/3/4
     */
    @Field(value = "STAGE_T")
    private String stageT;
    /**
     * 0/1/2/3/4
     */
    @Field(value = "STAGE_N")
    private String stageN;
    /**
     * 0/1/2/3/4
     */
    @Field(value = "STAGE_M")
    private String stageM;
    /**
     * 
     */
    @Field(value = "STAGE_CODE")
    private String stageCode;
    /**
     * 
     */
    @Field(value = "STAGE_NAME")
    private String stageName;
    /**
     * 1-根治性 2-姑息性 3-辅助性
     */
    @Field(value = "RADIOTH_MODE_CODE")
    private String radiothModeCode;
    /**
     * 
     */
    @Field(value = "RADIOTH_MODE_NAME")
    private String radiothModeName;
    /**
     * 1-连续 2-间断 3-分段
     */
    @Field(value = "RADIOTH_PROGR_CODE")
    private String radiothProgrCode;
    /**
     * 
     */
    @Field(value = "RADIOTH_PROGR_NAME")
    private String radiothProgrName;
    /**
     * 1-钴 2-直加 3-X线 4-后装
     */
    @Field(value = "RADIOTH_DEVICE_CODE")
    private String radiothDeviceCode;
    /**
     * 
     */
    @Field(value = "RADIOTH_DEVICE_NAME")
    private String radiothDeviceName;
    /**
     * 
     */
    @Field(value = "PRI_DOSE")
    private BigDecimal priDose;
    /**
     * 
     */
    @Field(value = "PRI_TIMES")
    private BigDecimal priTimes;
    /**
     * 
     */
    @Field(value = "PRI_DAYS")
    private BigDecimal priDays;
    /**
     * 
     */
    @Field(value = "PRI_DATE_BEGIN")
    private Date priDateBegin;
    /**
     * 
     */
    @Field(value = "PRI_DATE_END")
    private Date priDateEnd;
    /**
     * 
     */
    @Field(value = "LYM_DOSE")
    private BigDecimal lymDose;
    /**
     * 
     */
    @Field(value = "LYM_TIMES")
    private BigDecimal lymTimes;
    /**
     * 
     */
    @Field(value = "LYM_DAYS")
    private BigDecimal lymDays;
    /**
     * 
     */
    @Field(value = "LYM_DATE_BEGIN")
    private Date lymDateBegin;
    /**
     * 
     */
    @Field(value = "LYM_DATE_END")
    private Date lymDateEnd;
    /**
     * 
     */
    @Field(value = "MET_NAME")
    private String metName;
    /**
     * 
     */
    @Field(value = "MET_DOSE")
    private BigDecimal metDose;
    /**
     * 
     */
    @Field(value = "MET_TIMES")
    private BigDecimal metTimes;
    /**
     * 
     */
    @Field(value = "MET_DAYS")
    private BigDecimal metDays;
    /**
     * 
     */
    @Field(value = "MET_DATE_BEGIN")
    private Date metDateBegin;
    /**
     * 
     */
    @Field(value = "MET_DATE_END")
    private Date metDateEnd;
    /**
     * 1根治性 2姑息性 3新辅助性 4辅助性 5新药试用 6其他  
     */
    @Field(value = "CHEM_MODE_CODE")
    private String chemModeCode;
    /**
     * 
     */
    @Field(value = "CHEM_MODE_NAME")
    private String chemModeName;
    /**
     * 1-全化 2-动脉插管 3-胸腔注 4-腹腔注 5-髓注 6-其他  
     */
    @Field(value = "CHEM_METHOD_CODE")
    private String chemMethodCode;
    /**
     * 
     */
    @Field(value = "CHEM_METHOD_NAME")
    private String chemMethodName;
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
     * 1：P病理
2：C临床
     */
    public String getStageTypeCode(){
        return this.stageTypeCode;
    }

    /**
     * 1：P病理
2：C临床
     */
    public void setStageTypeCode(String stageTypeCode){
        this.stageTypeCode = stageTypeCode;
    }    
    /**
     * 
     */
    public String getStageTypeName(){
        return this.stageTypeName;
    }

    /**
     * 
     */
    public void setStageTypeName(String stageTypeName){
        this.stageTypeName = stageTypeName;
    }    
    /**
     * 0/1/2/3/4
     */
    public String getStageT(){
        return this.stageT;
    }

    /**
     * 0/1/2/3/4
     */
    public void setStageT(String stageT){
        this.stageT = stageT;
    }    
    /**
     * 0/1/2/3/4
     */
    public String getStageN(){
        return this.stageN;
    }

    /**
     * 0/1/2/3/4
     */
    public void setStageN(String stageN){
        this.stageN = stageN;
    }    
    /**
     * 0/1/2/3/4
     */
    public String getStageM(){
        return this.stageM;
    }

    /**
     * 0/1/2/3/4
     */
    public void setStageM(String stageM){
        this.stageM = stageM;
    }    
    /**
     * 
     */
    public String getStageCode(){
        return this.stageCode;
    }

    /**
     * 
     */
    public void setStageCode(String stageCode){
        this.stageCode = stageCode;
    }    
    /**
     * 
     */
    public String getStageName(){
        return this.stageName;
    }

    /**
     * 
     */
    public void setStageName(String stageName){
        this.stageName = stageName;
    }    
    /**
     * 1-根治性 2-姑息性 3-辅助性
     */
    public String getRadiothModeCode(){
        return this.radiothModeCode;
    }

    /**
     * 1-根治性 2-姑息性 3-辅助性
     */
    public void setRadiothModeCode(String radiothModeCode){
        this.radiothModeCode = radiothModeCode;
    }    
    /**
     * 
     */
    public String getRadiothModeName(){
        return this.radiothModeName;
    }

    /**
     * 
     */
    public void setRadiothModeName(String radiothModeName){
        this.radiothModeName = radiothModeName;
    }    
    /**
     * 1-连续 2-间断 3-分段
     */
    public String getRadiothProgrCode(){
        return this.radiothProgrCode;
    }

    /**
     * 1-连续 2-间断 3-分段
     */
    public void setRadiothProgrCode(String radiothProgrCode){
        this.radiothProgrCode = radiothProgrCode;
    }    
    /**
     * 
     */
    public String getRadiothProgrName(){
        return this.radiothProgrName;
    }

    /**
     * 
     */
    public void setRadiothProgrName(String radiothProgrName){
        this.radiothProgrName = radiothProgrName;
    }    
    /**
     * 1-钴 2-直加 3-X线 4-后装
     */
    public String getRadiothDeviceCode(){
        return this.radiothDeviceCode;
    }

    /**
     * 1-钴 2-直加 3-X线 4-后装
     */
    public void setRadiothDeviceCode(String radiothDeviceCode){
        this.radiothDeviceCode = radiothDeviceCode;
    }    
    /**
     * 
     */
    public String getRadiothDeviceName(){
        return this.radiothDeviceName;
    }

    /**
     * 
     */
    public void setRadiothDeviceName(String radiothDeviceName){
        this.radiothDeviceName = radiothDeviceName;
    }    
    /**
     * 
     */
    public BigDecimal getPriDose(){
        return this.priDose;
    }

    /**
     * 
     */
    public void setPriDose(BigDecimal priDose){
        this.priDose = priDose;
    }    
    /**
     * 
     */
    public BigDecimal getPriTimes(){
        return this.priTimes;
    }

    /**
     * 
     */
    public void setPriTimes(BigDecimal priTimes){
        this.priTimes = priTimes;
    }    
    /**
     * 
     */
    public BigDecimal getPriDays(){
        return this.priDays;
    }

    /**
     * 
     */
    public void setPriDays(BigDecimal priDays){
        this.priDays = priDays;
    }    
    /**
     * 
     */
    public Date getPriDateBegin(){
        return this.priDateBegin;
    }

    /**
     * 
     */
    public void setPriDateBegin(Date priDateBegin){
        this.priDateBegin = priDateBegin;
    }    
    /**
     * 
     */
    public Date getPriDateEnd(){
        return this.priDateEnd;
    }

    /**
     * 
     */
    public void setPriDateEnd(Date priDateEnd){
        this.priDateEnd = priDateEnd;
    }    
    /**
     * 
     */
    public BigDecimal getLymDose(){
        return this.lymDose;
    }

    /**
     * 
     */
    public void setLymDose(BigDecimal lymDose){
        this.lymDose = lymDose;
    }    
    /**
     * 
     */
    public BigDecimal getLymTimes(){
        return this.lymTimes;
    }

    /**
     * 
     */
    public void setLymTimes(BigDecimal lymTimes){
        this.lymTimes = lymTimes;
    }    
    /**
     * 
     */
    public BigDecimal getLymDays(){
        return this.lymDays;
    }

    /**
     * 
     */
    public void setLymDays(BigDecimal lymDays){
        this.lymDays = lymDays;
    }    
    /**
     * 
     */
    public Date getLymDateBegin(){
        return this.lymDateBegin;
    }

    /**
     * 
     */
    public void setLymDateBegin(Date lymDateBegin){
        this.lymDateBegin = lymDateBegin;
    }    
    /**
     * 
     */
    public Date getLymDateEnd(){
        return this.lymDateEnd;
    }

    /**
     * 
     */
    public void setLymDateEnd(Date lymDateEnd){
        this.lymDateEnd = lymDateEnd;
    }    
    /**
     * 
     */
    public String getMetName(){
        return this.metName;
    }

    /**
     * 
     */
    public void setMetName(String metName){
        this.metName = metName;
    }    
    /**
     * 
     */
    public BigDecimal getMetDose(){
        return this.metDose;
    }

    /**
     * 
     */
    public void setMetDose(BigDecimal metDose){
        this.metDose = metDose;
    }    
    /**
     * 
     */
    public BigDecimal getMetTimes(){
        return this.metTimes;
    }

    /**
     * 
     */
    public void setMetTimes(BigDecimal metTimes){
        this.metTimes = metTimes;
    }    
    /**
     * 
     */
    public BigDecimal getMetDays(){
        return this.metDays;
    }

    /**
     * 
     */
    public void setMetDays(BigDecimal metDays){
        this.metDays = metDays;
    }    
    /**
     * 
     */
    public Date getMetDateBegin(){
        return this.metDateBegin;
    }

    /**
     * 
     */
    public void setMetDateBegin(Date metDateBegin){
        this.metDateBegin = metDateBegin;
    }    
    /**
     * 
     */
    public Date getMetDateEnd(){
        return this.metDateEnd;
    }

    /**
     * 
     */
    public void setMetDateEnd(Date metDateEnd){
        this.metDateEnd = metDateEnd;
    }    
    /**
     * 1根治性 2姑息性 3新辅助性 4辅助性 5新药试用 6其他  
     */
    public String getChemModeCode(){
        return this.chemModeCode;
    }

    /**
     * 1根治性 2姑息性 3新辅助性 4辅助性 5新药试用 6其他  
     */
    public void setChemModeCode(String chemModeCode){
        this.chemModeCode = chemModeCode;
    }    
    /**
     * 
     */
    public String getChemModeName(){
        return this.chemModeName;
    }

    /**
     * 
     */
    public void setChemModeName(String chemModeName){
        this.chemModeName = chemModeName;
    }    
    /**
     * 1-全化 2-动脉插管 3-胸腔注 4-腹腔注 5-髓注 6-其他  
     */
    public String getChemMethodCode(){
        return this.chemMethodCode;
    }

    /**
     * 1-全化 2-动脉插管 3-胸腔注 4-腹腔注 5-髓注 6-其他  
     */
    public void setChemMethodCode(String chemMethodCode){
        this.chemMethodCode = chemMethodCode;
    }    
    /**
     * 
     */
    public String getChemMethodName(){
        return this.chemMethodName;
    }

    /**
     * 
     */
    public void setChemMethodName(String chemMethodName){
        this.chemMethodName = chemMethodName;
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