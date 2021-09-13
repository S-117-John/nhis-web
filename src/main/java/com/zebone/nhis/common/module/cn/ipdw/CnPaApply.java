package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: CN_PA_APPLY 
 *
 * @since 2016-09-12 10:30:28
 */
@Table(value="CN_PA_APPLY")
public class CnPaApply extends BaseModule  {

	@PK
	@Field(value="PK_ORDPE",id=KeyId.UUID)
    private String pkOrdpe;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="DISE_HIST")
    private String diseHist;

	@Field(value="SYMP")
    private String symp;

	@Field(value="SIGN")
    private String sign;

	@Field(value="DESC_OPT")
    private String descOpt;

	@Field(value="EXAM_RESULT")
    private String examResult;

	@Field(value="CLINIC_DIAG")
    private String clinicDiag;

	@Field(value="SAMP")
    private String samp;

	@Field(value="PART_BODY")
    private String partBody;

	@Field(value="DATE_FM")
    private Date dateFm;

	@Field(value="CYCLE")
    private Integer cycle;

	@Field(value="DURATION")
    private Integer duration;

	@Field(value="DATE_IM")
    private Date dateIm;

	@Field(value="TS_PREN")
    private Integer tsPren;

	@Field(value="TS_LABOR")
    private Integer tsLabor;

	@Field(value="TS_ABT")
    private Integer tsAbt;

	@Field(value="DATE_LL")
    private Date dateLl;

	@Field(value="DATE_FIND")
    private Date dateFind;

	@Field(value="PART_TUM")
    private String partTum;

	@Field(value="SIZE_TUM")
    private String sizeTum;

	@Field(value="GROW_TUM")
    private String growTum;

	@Field(value="FLAG_MET")
    private String flagMet;

	@Field(value="DESC_TREAT")
    private String descTreat;

	@Field(value="FLAG_PA")
    private String flagPa;

	@Field(value="PE_NO")
    private String peNo;

	@Field(value="PE_DIAG")
    private String peDiag;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkOrdpe(){
        return this.pkOrdpe;
    }
    public void setPkOrdpe(String pkOrdpe){
        this.pkOrdpe = pkOrdpe;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public String getDiseHist(){
        return this.diseHist;
    }
    public void setDiseHist(String diseHist){
        this.diseHist = diseHist;
    }

    public String getSymp(){
        return this.symp;
    }
    public void setSymp(String symp){
        this.symp = symp;
    }

    public String getSign(){
        return this.sign;
    }
    public void setSign(String sign){
        this.sign = sign;
    }

    public String getDescOpt(){
        return this.descOpt;
    }
    public void setDescOpt(String descOpt){
        this.descOpt = descOpt;
    }

    public String getExamResult(){
        return this.examResult;
    }
    public void setExamResult(String examResult){
        this.examResult = examResult;
    }

    public String getClinicDiag(){
        return this.clinicDiag;
    }
    public void setClinicDiag(String clinicDiag){
        this.clinicDiag = clinicDiag;
    }

    public String getSamp(){
        return this.samp;
    }
    public void setSamp(String samp){
        this.samp = samp;
    }

    public String getPartBody(){
        return this.partBody;
    }
    public void setPartBody(String partBody){
        this.partBody = partBody;
    }

    public Date getDateFm(){
        return this.dateFm;
    }
    public void setDateFm(Date dateFm){
        this.dateFm = dateFm;
    }

    public Integer getCycle(){
        return this.cycle;
    }
    public void setCycle(Integer cycle){
        this.cycle = cycle;
    }

    public Integer getDuration(){
        return this.duration;
    }
    public void setDuration(Integer duration){
        this.duration = duration;
    }

    public Date getDateIm(){
        return this.dateIm;
    }
    public void setDateIm(Date dateIm){
        this.dateIm = dateIm;
    }

    public Integer getTsPren(){
        return this.tsPren;
    }
    public void setTsPren(Integer tsPren){
        this.tsPren = tsPren;
    }

    public Integer getTsLabor(){
        return this.tsLabor;
    }
    public void setTsLabor(Integer tsLabor){
        this.tsLabor = tsLabor;
    }

    public Integer getTsAbt(){
        return this.tsAbt;
    }
    public void setTsAbt(Integer tsAbt){
        this.tsAbt = tsAbt;
    }

    public Date getDateLl(){
        return this.dateLl;
    }
    public void setDateLl(Date dateLl){
        this.dateLl = dateLl;
    }

    public Date getDateFind(){
        return this.dateFind;
    }
    public void setDateFind(Date dateFind){
        this.dateFind = dateFind;
    }

    public String getPartTum(){
        return this.partTum;
    }
    public void setPartTum(String partTum){
        this.partTum = partTum;
    }

    public String getSizeTum(){
        return this.sizeTum;
    }
    public void setSizeTum(String sizeTum){
        this.sizeTum = sizeTum;
    }

    public String getGrowTum(){
        return this.growTum;
    }
    public void setGrowTum(String growTum){
        this.growTum = growTum;
    }

    public String getFlagMet(){
        return this.flagMet;
    }
    public void setFlagMet(String flagMet){
        this.flagMet = flagMet;
    }

    public String getDescTreat(){
        return this.descTreat;
    }
    public void setDescTreat(String descTreat){
        this.descTreat = descTreat;
    }

    public String getFlagPa(){
        return this.flagPa;
    }
    public void setFlagPa(String flagPa){
        this.flagPa = flagPa;
    }

    public String getPeNo(){
        return this.peNo;
    }
    public void setPeNo(String peNo){
        this.peNo = peNo;
    }

    public String getPeDiag(){
        return this.peDiag;
    }
    public void setPeDiag(String peDiag){
        this.peDiag = peDiag;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}