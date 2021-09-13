package com.zebone.nhis.common.module.ex.oi;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EX_INFUSION_REG_DT 
 *
 * @since 2017-10-31 10:03:08
 */
@Table(value="EX_INFUSION_REG_DT")
public class ExInfusionRegDt extends BaseModule  {

	@PK
	@Field(value="PK_INFUREGDT",id=KeyId.UUID)
    private String pkInfuregdt;

	@Field(value="PK_INFUREG")
    private String pkInfureg;

	@Field(value="REG_DT_NO")
    private String regDtNo;

	@Field(value="EU_TYPE")
    private String euType;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="CODE_ORD")
    private String codeOrd;

	@Field(value="NAME_ORD")
    private String nameOrd;

	@Field(value="ORDSN")
    private Integer ordsn;

	@Field(value="ORDSN_PARENT")
    private Integer ordsnParent;

	@Field(value="CODE_FREQ")
    private String codeFreq;

	@Field(value="SPEC")
    private String spec;

	@Field(value="DOSAGE")
    private Double dosage;

	@Field(value="PK_UNIT_DOS")
    private String pkUnitDos;

	@Field(value="CODE_SUPPLY")
    private String codeSupply;

	@Field(value="QUAN")
    private Double quan;

	@Field(value="DAYS")
    private Long days;
	
	@Field(value="Note_SUPPLY")
    private String noteSupply;	

	@Field(value="EXEC_TIMES")
    private Long execTimes;

	@Field(value="REMAIN_TIMES")
    private Long remainTimes;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkInfuregdt(){
        return this.pkInfuregdt;
    }
    public void setPkInfuregdt(String pkInfuregdt){
        this.pkInfuregdt = pkInfuregdt;
    }

    public String getPkInfureg(){
        return this.pkInfureg;
    }
    public void setPkInfureg(String pkInfureg){
        this.pkInfureg = pkInfureg;
    }

    public String getRegDtNo(){
        return this.regDtNo;
    }
    public void setRegDtNo(String regDtNo){
        this.regDtNo = regDtNo;
    }

    public String getEuType(){
        return this.euType;
    }
    public void setEuType(String euType){
        this.euType = euType;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
    }

    public String getCodeOrd(){
        return this.codeOrd;
    }
    public void setCodeOrd(String codeOrd){
        this.codeOrd = codeOrd;
    }

    public String getNameOrd(){
        return this.nameOrd;
    }
    public void setNameOrd(String nameOrd){
        this.nameOrd = nameOrd;
    }

    public Integer getOrdsn(){
        return this.ordsn;
    }
    public void setOrdsn(Integer ordsn){
        this.ordsn = ordsn;
    }

    public Integer getOrdsnParent(){
        return this.ordsnParent;
    }
    public void setOrdsnParent(Integer ordsnParent){
        this.ordsnParent = ordsnParent;
    }

    public String getCodeFreq(){
        return this.codeFreq;
    }
    public void setCodeFreq(String codeFreq){
        this.codeFreq = codeFreq;
    }

    public String getSpec(){
        return this.spec;
    }
    public void setSpec(String spec){
        this.spec = spec;
    }

    public Double getDosage(){
        return this.dosage;
    }
    public void setDosage(Double dosage){
        this.dosage = dosage;
    }

    public String getPkUnitDos(){
        return this.pkUnitDos;
    }
    public void setPkUnitDos(String pkUnitDos){
        this.pkUnitDos = pkUnitDos;
    }

    public String getCodeSupply(){
        return this.codeSupply;
    }
    public void setCodeSupply(String codeSupply){
        this.codeSupply = codeSupply;
    }

    public Double getQuan(){
        return this.quan;
    }
    public void setQuan(Double quan){
        this.quan = quan;
    }

    public Long getDays(){
        return this.days;
    }
    public void setDays(Long days){
        this.days = days;
    }

	public String getNoteSupply() {
		return noteSupply;
	}
	public void setNoteSupply(String noteSupply) {
		this.noteSupply = noteSupply;
	}
    
    public Long getExecTimes(){
        return this.execTimes;
    }
    public void setExecTimes(Long execTimes){
        this.execTimes = execTimes;
    }

    public Long getRemainTimes(){
        return this.remainTimes;
    }
    public void setRemainTimes(Long remainTimes){
        this.remainTimes = remainTimes;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}