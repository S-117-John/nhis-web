package com.zebone.nhis.common.module.pi;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PI_DISE  - 患者信息-疾病史 
 *
 * @since 2016-09-14 01:57:38
 */
@Table(value="PI_DISE")
public class PiDise   {

    /** PK_PI_DISE - 疾病史主键 */
	@PK
	@Field(value="PK_PI_DISE",id=KeyId.UUID)
    private String pkPiDise;

    /** PK_PI - 患者 */
	@Field(value="PK_PI")
    private String pkPi;

    /** EU_MCSRC - 信息来源：0 患者就诊; 1 健康档案; 2 区域平台 */
	@Field(value="EU_MCSRC")
    private String euMcsrc;

    /** DT_MCTYPE - 医疗术语类型 */
	@Field(value="DT_MCTYPE")
    private String dtMctype;

    /** PK_DISE_PV - 疾病_就诊流程 */
	@Field(value="PK_DISE_PV")
    private String pkDisePv;

    /** PK_DISE_EHR - 疾病_健康档案 */
	@Field(value="PK_DISE_EHR")
    private String pkDiseEhr;

    /** PK_DISE_RHIP - 疾病_区域平台 */
	@Field(value="PK_DISE_RHIP")
    private String pkDiseRhip;

    /** NAME_DISE - 疾病名称 */
	@Field(value="NAME_DISE")
    private String nameDise;

    /** CODE_DISE - ICD编码 */
	@Field(value="CODE_DISE")
    private String codeDise;

    /** DATE_DIAG - 诊断日期 */
	@Field(value="DATE_DIAG")
    private Date dateDiag;

    /** FLAG_CURE - 治愈标志 */
	@Field(value="FLAG_CURE")
    private String flagCure;

    /** DATE_REC - 登记日期 */
	@Field(value="DATE_REC")
    private Date dateRec;

    /** PK_EMP_REC - 登记人编码 */
	@Field(value="PK_EMP_REC")
    private String pkEmpRec;

    /** NAME_EMP_REC - 登记人姓名 */
	@Field(value="NAME_EMP_REC")
    private String nameEmpRec;

    /** CREATOR - 创建人 */
	@Field(value="CREATOR",userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

    /** CREATE_TIME - 创建时间 */
	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

    /** MODIFIER - 修改人 */
	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

    /** DEL_FLAG - 删除标志 */
	@Field(value="DEL_FLAG")
    private String delFlag;

    /** TS - 时间戳 */
	@Field(value="TS",date=FieldType.ALL)
    private Date ts;


    public String getPkPiDise(){
        return this.pkPiDise;
    }
    public void setPkPiDise(String pkPiDise){
        this.pkPiDise = pkPiDise;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getEuMcsrc(){
        return this.euMcsrc;
    }
    public void setEuMcsrc(String euMcsrc){
        this.euMcsrc = euMcsrc;
    }

    public String getDtMctype(){
        return this.dtMctype;
    }
    public void setDtMctype(String dtMctype){
        this.dtMctype = dtMctype;
    }

    public String getPkDisePv(){
        return this.pkDisePv;
    }
    public void setPkDisePv(String pkDisePv){
        this.pkDisePv = pkDisePv;
    }

    public String getPkDiseEhr(){
        return this.pkDiseEhr;
    }
    public void setPkDiseEhr(String pkDiseEhr){
        this.pkDiseEhr = pkDiseEhr;
    }

    public String getPkDiseRhip(){
        return this.pkDiseRhip;
    }
    public void setPkDiseRhip(String pkDiseRhip){
        this.pkDiseRhip = pkDiseRhip;
    }

    public String getNameDise(){
        return this.nameDise;
    }
    public void setNameDise(String nameDise){
        this.nameDise = nameDise;
    }

    public String getCodeDise(){
        return this.codeDise;
    }
    public void setCodeDise(String codeDise){
        this.codeDise = codeDise;
    }

    public Date getDateDiag(){
        return this.dateDiag;
    }
    public void setDateDiag(Date dateDiag){
        this.dateDiag = dateDiag;
    }

    public String getFlagCure(){
        return this.flagCure;
    }
    public void setFlagCure(String flagCure){
        this.flagCure = flagCure;
    }

    public Date getDateRec(){
        return this.dateRec;
    }
    public void setDateRec(Date dateRec){
        this.dateRec = dateRec;
    }

    public String getPkEmpRec(){
        return this.pkEmpRec;
    }
    public void setPkEmpRec(String pkEmpRec){
        this.pkEmpRec = pkEmpRec;
    }

    public String getNameEmpRec(){
        return this.nameEmpRec;
    }
    public void setNameEmpRec(String nameEmpRec){
        this.nameEmpRec = nameEmpRec;
    }

    public String getCreator(){
        return this.creator;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }

    public Date getCreateTime(){
        return this.createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
}