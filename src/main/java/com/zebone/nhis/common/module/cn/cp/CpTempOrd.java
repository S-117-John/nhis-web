package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CP_TEMP_ORD 
 *
 * @since 2016-12-12 02:43:26
 */
@Table(value="CP_TEMP_ORD")
public class CpTempOrd extends BaseModule  {

	@PK
	@Field(value="PK_CPORD",id=KeyId.UUID)
    private String pkCpord;

	@Field(value="PK_CPPHASE")
    private String pkCpphase;

	@Field(value="SEQ")
    private Integer seq;

	@Field(value="CODE_ORDTYPE")
    private String codeOrdtype;

	@Field(value="EU_ORDTYPE")
    private String euOrdtype;

	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="CODE_SUPPLY")
    private String codeSupply;

	@Field(value="CODE_FREQ")
    private String codeFreq;

	@Field(value="EU_ALWAYS")
    private String euAlways;

	@Field(value="DOSAGE")
    private Double dosage;

	@Field(value="PK_UNIT_DOSE")
    private String pkUnitDose;

	@Field(value="QUAN")
    private Double quan;

	@Field(value="PK_UNIT")
    private String pkUnit;

	@Field(value="PK_PARENT")
    private String pkParent;

	@Field(value="FLAG_NEC")
    private String flagNec;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="DT_RISTYPE")
    private Date dtRistype;
	
    public String getPkCpord(){
        return this.pkCpord;
    }
    public void setPkCpord(String pkCpord){
        this.pkCpord = pkCpord;
    }

    public String getPkCpphase(){
        return this.pkCpphase;
    }
    public void setPkCpphase(String pkCpphase){
        this.pkCpphase = pkCpphase;
    }

    public Integer getSeq(){
        return this.seq;
    }
    public void setSeq(Integer seq){
        this.seq = seq;
    }

    public String getCodeOrdtype(){
        return this.codeOrdtype;
    }
    public void setCodeOrdtype(String codeOrdtype){
        this.codeOrdtype = codeOrdtype;
    }

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
    }

    public String getCodeSupply(){
        return this.codeSupply;
    }
    public void setCodeSupply(String codeSupply){
        this.codeSupply = codeSupply;
    }

    public String getCodeFreq(){
        return this.codeFreq;
    }
    public void setCodeFreq(String codeFreq){
        this.codeFreq = codeFreq;
    }

    public String getEuAlways(){
        return this.euAlways;
    }
    public void setEuAlways(String euAlways){
        this.euAlways = euAlways;
    }

    public Double getDosage(){
        return this.dosage;
    }
    public void setDosage(Double dosage){
        this.dosage = dosage;
    }

    public String getPkUnitDose(){
        return this.pkUnitDose;
    }
    public void setPkUnitDose(String pkUnitDose){
        this.pkUnitDose = pkUnitDose;
    }

    public Double getQuan(){
        return this.quan;
    }
    public void setQuan(Double quan){
        this.quan = quan;
    }

    public String getPkUnit(){
        return this.pkUnit;
    }
    public void setPkUnit(String pkUnit){
        this.pkUnit = pkUnit;
    }

    public String getPkParent(){
        return this.pkParent;
    }
    public void setPkParent(String pkParent){
        this.pkParent = pkParent;
    }

    public String getFlagNec(){
        return this.flagNec;
    }
    public void setFlagNec(String flagNec){
        this.flagNec = flagNec;
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
	/**
	 * 数据更新状态
	 */
	private String rowStatus;
	private String nameOrd;
	private String codeOrd;
	private String flagDrug;
	private String spec;
	private String namePhase;
	
	public String getRowStatus() {
		return rowStatus;
	}
	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}
	public String getNameOrd() {
		return nameOrd;
	}
	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}
	public String getFlagDrug() {
		return flagDrug;
	}
	public void setFlagDrug(String flagDrug) {
		this.flagDrug = flagDrug;
	}
	public String getEuOrdtype() {
		return euOrdtype;
	}
	public void setEuOrdtype(String euOrdtype) {
		this.euOrdtype = euOrdtype;
	}
	public Date getDtRistype() {
		return dtRistype;
	}
	public void setDtRistype(Date dtRistype) {
		this.dtRistype = dtRistype;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getCodeOrd() {
		return codeOrd;
	}
	public void setCodeOrd(String codeOrd) {
		this.codeOrd = codeOrd;
	}
	public String getNamePhase() {
		return namePhase;
	}
	public void setNamePhase(String namePhase) {
		this.namePhase = namePhase;
	}
	private String srvEuAlways;
	
	public String getSrvEuAlways() {
		return srvEuAlways;
	}
	public void setSrvEuAlways(String srvEuAlways) {
		this.srvEuAlways = srvEuAlways;
	}
}